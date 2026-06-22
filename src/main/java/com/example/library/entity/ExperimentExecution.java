package com.example.library.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class ExperimentExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "experiment_id")
    private Experiment experiment;

    @Enumerated(EnumType.STRING)
    private AttackType attack;

    @Enumerated(EnumType.STRING)
    private ExecutionStatus status = ExecutionStatus.PENDING;

    private Long executionTimeMs;

    private Long iterations;

    private Boolean successful;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    public ExperimentExecution() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Experiment getExperiment() { return experiment; }
    public void setExperiment(Experiment experiment) { this.experiment = experiment; }
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
