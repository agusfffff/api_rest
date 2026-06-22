package com.example.library.service;

import com.example.library.entity.Experiment;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.ExperimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ExperimentService {
    @Autowired
    private ExperimentRepository experimentRepository;

    public Page<Experiment> findAll(Pageable pageable) {
        return experimentRepository.findAll(pageable);
    }

    public Experiment findById(UUID id) {
        return experimentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experiment not found"));
    }

    public Experiment save(Experiment experiment) {
        return experimentRepository.save(experiment);
    }

    public void deleteById(UUID id) {
        if (!experimentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Experiment not found");
        }
        experimentRepository.deleteById(id);
    }
}
