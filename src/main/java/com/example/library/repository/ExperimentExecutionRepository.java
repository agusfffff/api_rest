package com.example.library.repository;

import com.example.library.entity.ExperimentExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;

@Repository
public interface ExperimentExecutionRepository extends JpaRepository<ExperimentExecution, UUID> {
    List<ExperimentExecution> findByExperimentId(UUID experimentId);
}
