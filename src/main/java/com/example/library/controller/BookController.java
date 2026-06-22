package com.example.library.controller;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;
import com.example.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Book management APIs")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    @Operation(summary = "List all books", description = "Retrieve a paginated list of all books")
    public ResponseEntity<Page<BookDTO>> getAllBooks(Pageable pageable) {
        Page<Book> books = bookService.findAll(pageable);
        Page<BookDTO> bookDTOs = books.map(this::convertToDTO);
        return ResponseEntity.ok(bookDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieve a single book by its ID")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        Book book = bookService.findById(id);
        return ResponseEntity.ok(convertToDTO(book));
    }

    @GetMapping("/search")
    @Operation(summary = "Search books by title", description = "Search books by title (case insensitive)")
    public ResponseEntity<List<BookDTO>> searchBooks(@RequestParam String title) {
        List<Book> books = bookService.searchByTitle(title);
        List<BookDTO> bookDTOs = books.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(bookDTOs);
    }

    @PostMapping
    @Operation(summary = "Create a new book", description = "Create a new book")
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        Book book = convertToEntity(bookDTO);
        Book savedBook = bookService.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedBook));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Update an existing book")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        Book existingBook = bookService.findById(id);
        existingBook.updateMetadata(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getIsbn(), bookDTO.getPublishedYear());
        Book updatedBook = bookService.save(existingBook);
        return ResponseEntity.ok(convertToDTO(updatedBook));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a book", description = "Delete a book by its ID")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private BookDTO convertToDTO(Book book) {
        return new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublishedYear(), book.isAvailable());
    }

    private Book convertToEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setPublishedYear(bookDTO.getPublishedYear());
        return book;
    }
}