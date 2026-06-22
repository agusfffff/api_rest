package com.example.library.controller;

import com.example.library.dto.LoanDTO;
import com.example.library.entity.Book;
import com.example.library.entity.Loan;
import com.example.library.entity.User;
import com.example.library.service.LoanService;
import com.example.library.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.library.security.CustomUserDetailsService;
import com.example.library.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @MockBean
    private UserService userService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllLoans() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        User user = new User("John", "john@example.com", "123");
        Book book = new Book("Title", "Author", "123", 2020);
        Loan loan = Loan.create(user, book, LocalDate.now().plusDays(14));
        List<Loan> loans = Arrays.asList(loan);
        Page<Loan> page = new PageImpl<>(loans, pageable, loans.size());
        when(loanService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void testCreateLoan() throws Exception {
        LoanDTO loanDTO = new LoanDTO(null, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(14), null, false, null, null);
        User user = new User("John", "john@example.com", "123");
        Book book = new Book("Title", "Author", "123", 2020);
        Loan loan = Loan.create(user, book, LocalDate.now().plusDays(14));
        loan.setId(1L);

        when(loanService.createLoan(1L, 1L, loanDTO.getDueDate())).thenReturn(loan);

        mockMvc.perform(post("/api/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loanDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testReturnBook() throws Exception {
        User user = new User("John", "john@example.com", "123");
        Book book = new Book("Title", "Author", "123", 2020);
        Loan loan = Loan.create(user, book, LocalDate.now().plusDays(14));
        loan.setId(1L);
        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());

        when(loanService.returnBook(1L)).thenReturn(loan);

        mockMvc.perform(put("/api/loans/1/return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.returned").value(true));
    }

    @Test
    void testGetOverdueLoans() throws Exception {
        User user = new User("John", "john@example.com", "123");
        Book book = new Book("Title", "Author", "123", 2020);
        Loan loan = Loan.create(user, book, LocalDate.now().minusDays(5));
        loan.setId(1L);
        loan.setLoanDate(LocalDate.now().minusDays(20));
        loan.setDueDate(LocalDate.now().minusDays(5));
        List<Loan> loans = Arrays.asList(loan);

        when(loanService.findOverdueLoans()).thenReturn(loans);

        mockMvc.perform(get("/api/loans/overdue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }


    @Test
    void testGetActiveLoansByUser() throws Exception {

        User userEntity = new User("John", "john@example.com", "123");
        userEntity.setId(1L);

        Book book = new Book("Title", "Author", "123", 2020);
        book.setId(1L);

        Loan loan = Loan.create(
                userEntity,
                book,
                LocalDate.now().plusDays(14)
        );

        List<Loan> loans = Arrays.asList(loan);

        when(userService.findByEmail(anyString()))
                .thenReturn(userEntity);

        when(loanService.findActiveLoansByUser(1L))
                .thenReturn(loans);

        mockMvc.perform(
                get("/api/loans/users/1")
                        .principal(() -> "john@example.com")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}