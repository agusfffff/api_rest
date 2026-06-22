package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = Arrays.asList(new Book("Title1", "Author1", "123", 2020), new Book("Title2", "Author2", "456", 2021));
        Page<Book> page = new PageImpl<>(books, pageable, books.size());
        when(bookRepository.findAll(pageable)).thenReturn(page);

        Page<Book> result = bookService.findAll(pageable);

        assertEquals(2, result.getContent().size());
        verify(bookRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindById_Success() {
        Book book = new Book("Title", "Author", "123", 2020);
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.findById(1L);

        assertEquals("Title", result.getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.findById(1L));
    }

    @Test
    void testSave() {
        Book book = new Book("Title", "Author", "123", 2020);
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.save(book);

        assertEquals("Title", result.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testDeleteById_Success() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> bookService.deleteById(1L));
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_NotFound() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> bookService.deleteById(1L));
    }

    @Test
    void testSearchByTitle() {
        List<Book> books = Arrays.asList(new Book("Title1", "Author1", "123", 2020));
        when(bookRepository.findByTitleContainingIgnoreCase("Title")).thenReturn(books);

        List<Book> result = bookService.searchByTitle("Title");

        assertEquals(1, result.size());
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase("Title");
    }
}