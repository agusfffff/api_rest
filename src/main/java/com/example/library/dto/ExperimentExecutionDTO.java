package com.example.library.dto;

import com.example.library.entity.AttackType;
import com.example.library.entity.ExecutionStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class ExperimentExecutionDTO {
    private UUID id;
    @NotNull private UUID experimentId;
    @NotNull private AttackType attack;
    private ExecutionStatus status;
    private Long executionTimeMs;
    private Long iterations;
    private Boolean successful;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    public ExperimentExecutionDTO() {}
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getExperimentId() { return experimentId; }
    public void setExperimentId(UUID experimentId) { this.experimentId = experimentId; }
    public AttackType getAttack() { return attack; }
    public void setAttack(AttackType attack) { this.attack = attack; }
    public ExecutionStatus getStatus() { return status; }
    public void setStatus(ExecutionStatus status) { this.status = status; }
    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
    public Long getIterations() { return iterations; }
    public void setIterations(Long iterations) { this.iterations = iterations; }
    public Boolean getSuccessful() { return successful; }
    public void setSuccessful(Boolean successful) { this.successful = successful; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getFinishedAt() { return finishedAt; }
    public void setFinishedAt(LocalDateTime finishedAt) { this.finishedAt = finishedAt; }
}
