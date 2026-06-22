package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.entity.Loan;
import com.example.library.entity.User;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        User user = new User("John", "john@example.com", "123");
        Book book = new Book("Title", "Author", "123", 2020);
        Loan loan = Loan.create(user, book, LocalDate.now().plusDays(14));
        List<Loan> loans = Arrays.asList(loan);
        Page<Loan> page = new PageImpl<>(loans, pageable, loans.size());
        when(loanRepository.findAll(pageable)).thenReturn(page);

        Page<Loan> result = loanService.findAll(pageable);

        assertEquals(1, result.getContent().size());
        verify(loanRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindById_Success() {
        User user = new User("John", "john@example.com", "123");
        Book book = new Book("Title", "Author", "123", 2020);
        Loan loan = Loan.create(user, book, LocalDate.now().plusDays(14));
        loan.setId(1L);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        Loan result = loanService.findById(1L);

        assertEquals(1L, result.getId());
        verify(loanRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> loanService.findById(1L));
    }

    @Test
    void testCreateLoan_Success() {
        User user = new User("John", "john@example.com", "123");
        user.setId(1L);
        Book book = new Book("Title", "Author", "123", 2020);
        book.setId(1L);
        LocalDate dueDate = LocalDate.now().plusDays(14);

        when(userService.findById(1L)).thenReturn(user);
        when(bookService.findById(1L)).thenReturn(book);
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Loan result = loanService.createLoan(1L, 1L, dueDate);

        assertEquals(user, result.getUser());
        assertEquals(book, result.getBook());
        assertFalse(book.isAvailable());
        verify(bookService, times(1)).save(book);
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void testCreateLoan_BookNotAvailable() {
        User user = new User("John", "john@example.com", "123");
        user.setId(1L);
        Book book = new Book("Title", "Author", "123", 2020);
        book.setId(1L);
        book.markAsLoaned();
        LocalDate dueDate = LocalDate.now().plusDays(14);

        when(userService.findById(1L)).thenReturn(user);
        when(bookService.findById(1L)).thenReturn(book);

        assertThrows(BadRequestException.class, () -> loanService.createLoan(1L, 1L, dueDate));
    }

    @Test
    void testReturnBook_Success() {
        User user = new User("John", "john@example.com", "123");
        Book book = new Book("Title", "Author", "123", 2020);
        book.markAsLoaned();
        Loan loan = Loan.create(user, book, LocalDate.now().plusDays(14));
        loan.setId(1L);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(loan)).thenReturn(loan);

        Loan result = loanService.returnBook(1L);

        assertTrue(result.isReturned());
        assertNotNull(result.getReturnDate());
        assertTrue(book.isAvailable());
        verify(bookService, times(1)).save(book);
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void testReturnBook_AlreadyReturned() {
        User user = new User("John", "john@example.com", "123");
        Book book = new Book("Title", "Author", "123", 2020);
        Loan loan = Loan.create(user, book, LocalDate.now().plusDays(14));
        loan.setId(1L);
        loan.setReturned(true);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        assertThrows(BadRequestException.class, () -> loanService.returnBook(1L));
    }

    @Test
    void testFindActiveLoansByUser() {
        List<Loan> loans = Arrays.asList(new Loan());
        when(loanRepository.findByUserIdAndReturnedFalse(1L)).thenReturn(loans);

        List<Loan> result = loanService.findActiveLoansByUser(1L);

        assertEquals(1, result.size());
        verify(loanRepository, times(1)).findByUserIdAndReturnedFalse(1L);
    }

    @Test
    void testFindOverdueLoans() {
        List<Loan> loans = Arrays.asList(new Loan());
        when(loanRepository.findOverdueLoans(any(LocalDate.class))).thenReturn(loans);

        List<Loan> result = loanService.findOverdueLoans();

        assertEquals(1, result.size());
        verify(loanRepository, times(1)).findOverdueLoans(any(LocalDate.class));
    }
}