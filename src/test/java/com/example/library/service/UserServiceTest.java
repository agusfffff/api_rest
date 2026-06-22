package com.example.library.service;

import com.example.library.entity.User;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = Arrays.asList(new User("John", "john@example.com", "123"), new User("Jane", "jane@example.com", "456"));
        Page<User> page = new PageImpl<>(users, pageable, users.size());
        when(userRepository.findAll(pageable)).thenReturn(page);

        Page<User> result = userService.findAll(pageable);

        assertEquals(2, result.getContent().size());
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindById_Success() {
        User user = new User("John", "john@example.com", "123");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertEquals("John", result.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void testSave() {
        User user = new User("John", "john@example.com", "123");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.save(user);

        assertEquals("John", result.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteById_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteById(1L));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteById(1L));
    }

    @Test
    void testFindByEmail_Success() {
        User user = new User("John", "john@example.com", "123");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        User result = userService.findByEmail("john@example.com");

        assertEquals("john@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail("john@example.com");
    }

    @Test
    void testFindByEmail_NotFound() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findByEmail("john@example.com"));
    }
}