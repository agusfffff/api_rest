package com.example.library;

import com.example.library.dto.ExperimentDTO;
import com.example.library.entity.AlgorithmType;
import com.example.library.entity.Difficulty;
import com.example.library.repository.ExperimentRepository;
import com.example.library.repository.ResearchUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LibraryApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private ResearchUserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        experimentRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateAndGetExperiment() throws Exception {
        ExperimentDTO dto = new ExperimentDTO();
        dto.setName("Integration Test");
        dto.setAlgorithm(AlgorithmType.CAESAR);
        dto.setDifficulty(Difficulty.EASY);

        String json = mockMvc.perform(post("/api/experiments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Integration Test"))
                .andReturn().getResponse().getContentAsString();

        ExperimentDTO created = objectMapper.readValue(json, ExperimentDTO.class);

        mockMvc.perform(get("/api/experiments/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Test"));
    }
}