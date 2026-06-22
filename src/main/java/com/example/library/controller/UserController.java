package com.example.library.controller;

import com.example.library.dto.UserDTO;
import com.example.library.entity.User;
import com.example.library.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management APIs")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all users", description = "Retrieve a paginated list of all users")
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        Page<User> users = userService.findAll(pageable);
        Page<UserDTO> userDTOs = users.map(this::convertToDTO);
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN') or principal.username == @userService.findById(#id).email")
    @Operation(summary = "Get user by ID", description = "Retrieve a single user by its ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(convertToDTO(user));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new user", description = "Register a new user")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        User savedUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a user", description = "Update an existing user")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        User existingUser = userService.findById(id);
        existingUser.updateProfile(userDTO.getName(), userDTO.getEmail(), userDTO.getPhone());
        if (userDTO.isActive()) {
            existingUser.activate();
        } else {
            existingUser.deactivate();
        }
        User updatedUser = userService.save(existingUser);
        return ResponseEntity.ok(convertToDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a user", description = "Delete a user by its ID")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.isActive());
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        return user;
    }
}