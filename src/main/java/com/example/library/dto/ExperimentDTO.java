package com.example.library.dto;

import com.example.library.entity.AlgorithmType;
import com.example.library.entity.Difficulty;
import com.example.library.entity.ExperimentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class ExperimentDTO {
    private UUID id;
    @NotBlank private String name;
    @NotNull private AlgorithmType algorithm;
    @NotNull private Difficulty difficulty;
    private String publicPayload;
    private String encryptedPayload;
    private ExperimentStatus status;
    private LocalDateTime createdAt;

    public ExperimentDTO() {}
    // Getters and Setters...
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public AlgorithmType getAlgorithm() { return algorithm; }
    public void setAlgorithm(AlgorithmType algorithm) { this.algorithm = algorithm; }
    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public String getPublicPayload() { return publicPayload; }
    public void setPublicPayload(String publicPayload) { this.publicPayload = publicPayload; }
    public String getEncryptedPayload() { return encryptedPayload; }
    public void setEncryptedPayload(String encryptedPayload) { this.encryptedPayload = encryptedPayload; }
    public ExperimentStatus getStatus() { return status; }
    public void setStatus(ExperimentStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
