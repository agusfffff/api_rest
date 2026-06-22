package com.example.library.controller;

import com.example.library.dto.ResearchUserDTO;
import com.example.library.entity.ResearchUser;
import com.example.library.service.ResearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class ResearchUserController {
    @Autowired
    private ResearchUserService researchUserService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ResearchUserDTO>> getAllUsers(Pageable pageable) {
        Page<ResearchUser> users = researchUserService.findAll(pageable);
        Page<ResearchUserDTO> dtos = users.map(this::convertToDTO);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.username == @researchUserService.findById(#id).username")
    public ResponseEntity<ResearchUserDTO> getUserById(@PathVariable Long id) {
        ResearchUser user = researchUserService.findById(id);
        return ResponseEntity.ok(convertToDTO(user));
    }

    private ResearchUserDTO convertToDTO(ResearchUser user) {
        ResearchUserDTO dto = new ResearchUserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRoles(user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()));
        return dto;
    }
}
