package com.example.library.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "execution_id")
    private ExperimentExecution execution;

    @Column(columnDefinition = "TEXT")
    private String findings;

    @Column(columnDefinition = "TEXT")
    private String recoveredSecret;

    @Column(columnDefinition = "TEXT")
    private String metricsJson;

    public Report() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public ExperimentExecution getExecution() { return execution; }
    public void setExecution(ExperimentExecution execution) { this.execution = execution; }
    public String getFindings() { return findings; }
    public void setFindings(String findings) { this.findings = findings; }
    public String getRecoveredSecret() { return recoveredSecret; }
    public void setRecoveredSecret(String recoveredSecret) { this.recoveredSecret = recoveredSecret; }
    public String getMetricsJson() { return metricsJson; }
    public void setMetricsJson(String metricsJson) { this.metricsJson = metricsJson; }
}
