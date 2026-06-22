package com.example.library.repository;

import com.example.library.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
    Optional<Report> findByExecutionId(UUID executionId);
}
