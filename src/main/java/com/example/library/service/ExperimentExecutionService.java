package com.example.library.service;

import com.example.library.entity.ExperimentExecution;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.ExperimentExecutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;

@Service
public class ExperimentExecutionService {
    @Autowired
    private ExperimentExecutionRepository experimentExecutionRepository;

    public ExperimentExecution findById(UUID id) {
        return experimentExecutionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Execution not found"));
    }

    public List<ExperimentExecution> findByExperimentId(UUID experimentId) {
        return experimentExecutionRepository.findByExperimentId(experimentId);
    }

    public ExperimentExecution save(ExperimentExecution execution) {
        return experimentExecutionRepository.save(execution);
    }
}
