package com.example.library.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @NotNull
    private Book book;

    @NotNull
    private LocalDate loanDate;

    @NotNull
    private LocalDate dueDate;

    private LocalDate returnDate;

    private boolean returned = false;

    // Constructors
    public Loan() {}

    private Loan(User user, Book book, LocalDate loanDate, LocalDate dueDate) {
        this.user = user;
        this.book = book;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
    }

    public static Loan create(User user, Book book, LocalDate dueDate) {
        if (user == null) {
            throw new IllegalArgumentException("User is required to create a loan");
        }
        if (book == null) {
            throw new IllegalArgumentException("Book is required to create a loan");
        }
        if (dueDate == null) {
            throw new IllegalArgumentException("Due date is required to create a loan");
        }
        return new Loan(user, book, LocalDate.now(), dueDate);
    }

    public void markReturned() {
        if (this.returned) {
            throw new IllegalStateException("Loan is already returned");
        }
        this.returned = true;
        this.returnDate = LocalDate.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }
}