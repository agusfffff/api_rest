package com.example.library.controller;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;
import com.example.library.service.BookService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllBooks() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = Arrays.asList(new Book("Title1", "Author1", "123", 2020));
        Page<Book> page = new PageImpl<>(books, pageable, books.size());
        when(bookService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Title1"));
    }

    @Test
    void testGetBookById() throws Exception {
        Book book = new Book("Title", "Author", "123", 2020);
        book.setId(1L);
        when(bookService.findById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    void testSearchBooks() throws Exception {
        List<Book> books = Arrays.asList(new Book("Title", "Author", "123", 2020));
        when(bookService.searchByTitle("Title")).thenReturn(books);

        mockMvc.perform(get("/api/books/search").param("title", "Title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Title"));
    }

    @Test
    void testCreateBook() throws Exception {
        BookDTO bookDTO = new BookDTO(null, "New Title", "New Author", "456", 2023, true);
        Book savedBook = new Book("New Title", "New Author", "456", 2023);
        savedBook.setId(1L);
        when(bookService.save(any(Book.class))).thenReturn(savedBook);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Title"));
    }

    @Test
    void testUpdateBook() throws Exception {
        Book existingBook = new Book("Old Title", "Old Author", "123", 2020);
        existingBook.setId(1L);
        BookDTO bookDTO = new BookDTO(1L, "Updated Title", "Updated Author", "123", 2020, true);
        Book updatedBook = new Book("Updated Title", "Updated Author", "123", 2020);
        updatedBook.setId(1L);

        when(bookService.findById(1L)).thenReturn(existingBook);
        when(bookService.save(any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteById(1L);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }
}