package com.example.library;

import com.example.library.dto.BookDTO;
import com.example.library.dto.JwtAuthResponse;
import com.example.library.dto.RegisterRequest;
import com.example.library.entity.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LibraryApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testCreateAndGetBook() throws Exception {
        BookDTO bookDTO = new BookDTO(null, "Integration Test Book", "Test Author", "999999", 2023, true);

        // Create book
        String bookJson = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Integration Test Book"))
                .andReturn().getResponse().getContentAsString();

        BookDTO createdBook = objectMapper.readValue(bookJson, BookDTO.class);

        // Get book by ID
        mockMvc.perform(get("/api/books/" + createdBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Test Book"));
    }

    @Test
    void testCreateAndGetUser() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Integration User");
        registerRequest.setEmail("integration@example.com");
        registerRequest.setPassword("StrongPassword123");
        registerRequest.setPhone("555-1234");

        // Register user and receive JWT token
        String authJson = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        JwtAuthResponse authResponse = objectMapper.readValue(authJson, JwtAuthResponse.class);
        String token = authResponse.getAccessToken();

        User createdUser = userRepository.findByEmail("integration@example.com").orElseThrow();

        // Get user by ID using bearer token
        mockMvc.perform(get("/api/users/" + createdUser.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("integration@example.com"));
    }

    @Test
    void testBookNotFound() throws Exception {
        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void testValidationError() throws Exception {
        BookDTO invalidBookDTO = new BookDTO(null, "", "", "", null, true); // Invalid data

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBookDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }
}