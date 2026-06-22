package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.entity.Loan;
import com.example.library.entity.User;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    public Page<Loan> findAll(Pageable pageable) {
        return loanRepository.findAll(pageable);
    }

    public Loan findById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
    }
    
    @Transactional
    public Loan createLoan(Long userId, Long bookId, LocalDate dueDate) {
        User user = userService.findById(userId);
        Book book = bookService.findById(bookId);

        if (!book.isAvailable()) {
            throw new BadRequestException("Book is not available for loan");
        }

        book.markAsLoaned();
        bookService.save(book);

        Loan loan = Loan.create(user, book, dueDate);
        return loanRepository.save(loan);
    }

    @Transactional
    public Loan returnBook(Long loanId) {
        Loan loan = findById(loanId);

        if (loan.isReturned()) {
            throw new BadRequestException("Loan is already returned");
        }

        loan.markReturned();

        Book book = loan.getBook();
        book.markAsReturned();
        bookService.save(book);

        return loanRepository.save(loan);
    }

    public List<Loan> findActiveLoansByUser(Long userId) {
        return loanRepository.findByUserIdAndReturnedFalse(userId);
    }

    public List<Loan> findOverdueLoans() {
        return loanRepository.findOverdueLoans(LocalDate.now());
    }
}