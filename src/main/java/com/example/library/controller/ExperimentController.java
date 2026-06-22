package com.example.library.controller;

import com.example.library.dto.ExperimentDTO;
import com.example.library.entity.Experiment;
import com.example.library.service.ExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/experiments")
public class ExperimentController {
    @Autowired
    private ExperimentService experimentService;

    @GetMapping
    public ResponseEntity<Page<ExperimentDTO>> getAllExperiments(Pageable pageable) {
        Page<Experiment> experiments = experimentService.findAll(pageable);
        Page<ExperimentDTO> dtos = experiments.map(this::convertToDTO);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperimentDTO> getExperimentById(@PathVariable UUID id) {
        Experiment exp = experimentService.findById(id);
        return ResponseEntity.ok(convertToDTO(exp));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExperimentDTO> createExperiment(@Valid @RequestBody ExperimentDTO dto) {
        Experiment exp = convertToEntity(dto);
        Experiment saved = experimentService.save(exp);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
    }

    private ExperimentDTO convertToDTO(Experiment exp) {
        ExperimentDTO dto = new ExperimentDTO();
        dto.setId(exp.getId());
        dto.setName(exp.getName());
        dto.setAlgorithm(exp.getAlgorithm());
        dto.setDifficulty(exp.getDifficulty());
        dto.setPublicPayload(exp.getPublicPayload());
        dto.setEncryptedPayload(exp.getEncryptedPayload());
        dto.setStatus(exp.getStatus());
        dto.setCreatedAt(exp.getCreatedAt());
        return dto;
    }

    private Experiment convertToEntity(ExperimentDTO dto) {
        Experiment exp = new Experiment();
        exp.setName(dto.getName());
        exp.setAlgorithm(dto.getAlgorithm());
        exp.setDifficulty(dto.getDifficulty());
        exp.setPublicPayload(dto.getPublicPayload());
        exp.setEncryptedPayload(dto.getEncryptedPayload());
        if (dto.getStatus() != null) exp.setStatus(dto.getStatus());
        return exp;
    }
}
