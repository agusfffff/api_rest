package com.example.library.repository;

import com.example.library.entity.ResearchUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ResearchUserRepository extends JpaRepository<ResearchUser, Long> {
    Optional<ResearchUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
