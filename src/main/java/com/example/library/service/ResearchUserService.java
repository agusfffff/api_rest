package com.example.library.service;

import com.example.library.entity.ResearchUser;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.ResearchUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ResearchUserService {
    @Autowired
    private ResearchUserRepository researchUserRepository;

    public Page<ResearchUser> findAll(Pageable pageable) {
        return researchUserRepository.findAll(pageable);
    }

    public ResearchUser findById(Long id) {
        return researchUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public ResearchUser findByUsername(String username) {
        return researchUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public ResearchUser save(ResearchUser user) {
        return researchUserRepository.save(user);
    }

    public void deleteById(Long id) {
        if (!researchUserRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        researchUserRepository.deleteById(id);
    }
}
