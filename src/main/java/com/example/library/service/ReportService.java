package com.example.library.service;

import com.example.library.entity.Report;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;

    public Report findById(UUID id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
    }

    public Report findByExecutionId(UUID executionId) {
        return reportRepository.findByExecutionId(executionId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found for execution"));
    }

    public Report save(Report report) {
        return reportRepository.save(report);
    }
}
