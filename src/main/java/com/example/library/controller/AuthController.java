package com.example.library.controller;

import com.example.library.dto.JwtAuthResponse;
import com.example.library.dto.LoginRequest;
import com.example.library.dto.RegisterRequest;
import com.example.library.entity.User;
import com.example.library.security.JwtTokenProvider;
import com.example.library.security.Role;
import com.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication.getName());
        return ResponseEntity.ok(new JwtAuthResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPhone(registerRequest.getPhone());
        user.setRoles(Collections.singleton(Role.USER));

        userService.save(user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword())
        );
        String jwt = tokenProvider.generateToken(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(new JwtAuthResponse(jwt));
    }
}
