package com.example.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class LoanDTO {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long bookId;

    private LocalDate loanDate;

    private LocalDate dueDate;

    private LocalDate returnDate;

    private boolean returned;

    // For response, include names
    private String userName;
    private String bookTitle;

    // Constructors
    public LoanDTO() {}

    public LoanDTO(Long id, Long userId, Long bookId, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate, boolean returned, String userName, String bookTitle) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.returned = returned;
        this.userName = userName;
        this.bookTitle = bookTitle;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}