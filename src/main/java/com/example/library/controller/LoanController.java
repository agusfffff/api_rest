package com.example.library.controller;

import com.example.library.dto.LoanDTO;
import com.example.library.entity.Loan;
import com.example.library.service.LoanService;
import com.example.library.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
@Tag(name = "Loans", description = "Loan management APIs")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "List all loans", description = "Retrieve a paginated list of all loans")
    public ResponseEntity<Page<LoanDTO>> getAllLoans(Pageable pageable) {
        Page<Loan> loans = loanService.findAll(pageable);
        Page<LoanDTO> loanDTOs = loans.map(this::convertToDTO);
        return ResponseEntity.ok(loanDTOs);
    }

    @PostMapping
    @Operation(summary = "Create a new loan", description = "Register a new loan")
    public ResponseEntity<LoanDTO> createLoan(@RequestBody LoanDTO loanDTO) {
        Loan loan = loanService.createLoan(loanDTO.getUserId(), loanDTO.getBookId(), loanDTO.getDueDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(loan));
    }

    @PutMapping("/{id}/return")
    @Operation(summary = "Return a book", description = "Mark a loan as returned")
    public ResponseEntity<LoanDTO> returnBook(@PathVariable Long id) {
        Loan loan = loanService.returnBook(id);
        return ResponseEntity.ok(convertToDTO(loan));
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue loans", description = "Retrieve all overdue loans")
    public ResponseEntity<List<LoanDTO>> getOverdueLoans() {
        List<Loan> loans = loanService.findOverdueLoans();
        List<LoanDTO> loanDTOs = loans.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(loanDTOs);
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get active loans by user", description = "Retrieve active loans for a specific user")
    public ResponseEntity<List<LoanDTO>> getActiveLoansByUser(@PathVariable Long userId, Principal principal) {
        verifyCurrentUserOrAdmin(userId, principal);
        List<Loan> loans = loanService.findActiveLoansByUser(userId);
        List<LoanDTO> loanDTOs = loans.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(loanDTOs);
    }

    private void verifyCurrentUserOrAdmin(Long userId, Principal principal) {
        if (principal == null) {
            throw new AccessDeniedException("Access denied");
        }

        if (principal instanceof Authentication authentication) {
            if (authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
                return;
            }
            if (authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_LIBRARIAN"))) {
                return;
            }
        }

        String currentUserEmail = principal.getName();
        Long currentUserId = userService.findByEmail(currentUserEmail).getId();
        if (!currentUserId.equals(userId)) {
            throw new AccessDeniedException("Access denied");
        }
    }

    private LoanDTO convertToDTO(Loan loan) {
        return new LoanDTO(
            loan.getId(),
            loan.getUser().getId(),
            loan.getBook().getId(),
            loan.getLoanDate(),
            loan.getDueDate(),
            loan.getReturnDate(),
            loan.isReturned(),
            loan.getUser().getName(),
            loan.getBook().getTitle()
        );
    }
}