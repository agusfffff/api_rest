package com.example.library.dto;

import java.util.UUID;

public class ReportDTO {
    private UUID id;
    private UUID executionId;
    private String findings;
    private String recoveredSecret;
    private String metricsJson;

    public ReportDTO() {}
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getExecutionId() { return executionId; }
    public void setExecutionId(UUID executionId) { this.executionId = executionId; }
    public String getFindings() { return findings; }
    public void setFindings(String findings) { this.findings = findings; }
    public String getRecoveredSecret() { return recoveredSecret; }
    public void setRecoveredSecret(String recoveredSecret) { this.recoveredSecret = recoveredSecret; }
    public String getMetricsJson() { return metricsJson; }
    public void setMetricsJson(String metricsJson) { this.metricsJson = metricsJson; }
}
