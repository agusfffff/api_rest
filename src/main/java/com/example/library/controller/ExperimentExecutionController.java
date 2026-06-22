package com.example.library.controller;

import com.example.library.dto.ExperimentExecutionDTO;
import com.example.library.entity.ExperimentExecution;
import com.example.library.service.ExperimentExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/executions")
public class ExperimentExecutionController {

    @Autowired
    private ExperimentExecutionService experimentExecutionService;

    @GetMapping("/{id}")
    public ResponseEntity<ExperimentExecutionDTO> getExecutionById(@PathVariable UUID id) {
        ExperimentExecution exec = experimentExecutionService.findById(id);
        return ResponseEntity.ok(convertToDTO(exec));
    }

    @GetMapping("/experiment/{experimentId}")
    public ResponseEntity<List<ExperimentExecutionDTO>> getByExperimentId(@PathVariable UUID experimentId) {
        List<ExperimentExecution> executions = experimentExecutionService.findByExperimentId(experimentId);
        List<ExperimentExecutionDTO> dtos = executions.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private ExperimentExecutionDTO convertToDTO(ExperimentExecution exec) {
        ExperimentExecutionDTO dto = new ExperimentExecutionDTO();
        dto.setId(exec.getId());
        dto.setExperimentId(exec.getExperiment().getId());
        dto.setAttack(exec.getAttack());
        dto.setStatus(exec.getStatus());
        dto.setExecutionTimeMs(exec.getExecutionTimeMs());
        dto.setIterations(exec.getIterations());
        dto.setSuccessful(exec.getSuccessful());
        dto.setStartedAt(exec.getStartedAt());
        dto.setFinishedAt(exec.getFinishedAt());
        return dto;
    }
}
