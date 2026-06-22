package com.example.library.repository;

import com.example.library.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByUserIdAndReturnedFalse(Long userId);

    @Query("SELECT l FROM Loan l WHERE l.dueDate < :currentDate AND l.returned = false")
    List<Loan> findOverdueLoans(LocalDate currentDate);
}