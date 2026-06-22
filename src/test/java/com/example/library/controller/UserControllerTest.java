package com.example.library.controller;

import com.example.library.dto.UserDTO;
import com.example.library.entity.User;
import com.example.library.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.library.security.CustomUserDetailsService;
import com.example.library.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllUsers() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = Arrays.asList(new User("John", "john@example.com", "123"));
        Page<User> page = new PageImpl<>(users, pageable, users.size());
        when(userService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("John"));
    }

    @Test
    void testGetUserById() throws Exception {
        User user = new User("John", "john@example.com", "123");
        user.setId(1L);
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1").with(user("john@example.com").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void testCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO(null, "Jane", "jane@example.com", "secret123", "456", true);
        User savedUser = new User("Jane", "jane@example.com", "secret123");
        savedUser.setId(2L);
        when(userService.save(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Jane"));
    }

    @Test
    void testUpdateUser() throws Exception {
        User existingUser = new User("John", "john@example.com", "123");
        existingUser.setId(1L);
        UserDTO userDTO = new UserDTO(1L, "John Updated", "john@example.com", "123", true);
        User updatedUser = new User("John Updated", "john@example.com", "123");
        updatedUser.setId(1L);

        when(userService.findById(1L)).thenReturn(existingUser);
        when(userService.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteById(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
}