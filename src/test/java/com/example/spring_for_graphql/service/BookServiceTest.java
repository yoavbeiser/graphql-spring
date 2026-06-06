package com.example.spring_for_graphql.service;

import com.example.spring_for_graphql.exception.ResourceNotFoundException;
import com.example.spring_for_graphql.model.Book;
import com.example.spring_for_graphql.repository.AuthorRepository;
import com.example.spring_for_graphql.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock BookRepository bookRepository;
    @Mock AuthorRepository authorRepository;
    @InjectMocks BookService bookService;

    @Test
    void findAll_returnsList() {
        var book = new Book("b1", "Dune", "SciFi", 1965, "a1");
        when(bookRepository.findAll()).thenReturn(List.of(book));
        assertThat(bookService.findAll()).containsExactly(book);
    }

    @Test
    void findById_found() {
        var book = new Book("b1", "Dune", "SciFi", 1965, "a1");
        when(bookRepository.findById("b1")).thenReturn(Optional.of(book));
        assertThat(bookService.findById("b1")).contains(book);
    }

    @Test
    void findById_notFound() {
        when(bookRepository.findById("x")).thenReturn(Optional.empty());
        assertThat(bookService.findById("x")).isEmpty();
    }

    @Test
    void findByGenre_returnsFiltered() {
        var book = new Book("b1", "Dune", "SciFi", 1965, "a1");
        when(bookRepository.findByGenre("SciFi")).thenReturn(List.of(book));
        assertThat(bookService.findByGenre("SciFi")).containsExactly(book);
    }

    @Test
    void searchByTitle_returnsMatches() {
        var book = new Book("b1", "Dune", "SciFi", 1965, "a1");
        when(bookRepository.findByTitleContainingIgnoreCase("dune")).thenReturn(List.of(book));
        assertThat(bookService.searchByTitle("dune")).containsExactly(book);
    }

    @Test
    void findByYearRange_valid() {
        var book = new Book("b1", "Dune", "SciFi", 1965, "a1");
        when(bookRepository.findByPublishedYearBetween(1960, 1970)).thenReturn(List.of(book));
        assertThat(bookService.findByYearRange(1960, 1970)).containsExactly(book);
    }

    @Test
    void findByYearRange_fromGreaterThanTo_throws() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> bookService.findByYearRange(2020, 2000))
                .withMessageContaining("'from' year");
    }

    @Test
    void create_success() {
        when(authorRepository.existsById("a1")).thenReturn(true);
        var saved = new Book("b1", "Dune", "SciFi", 1965, "a1");
        when(bookRepository.save(any(Book.class))).thenReturn(saved);
        assertThat(bookService.create("Dune", "SciFi", 1965, "a1")).isEqualTo(saved);
    }

    @Test
    void create_blankTitle_throws() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> bookService.create("  ", "SciFi", 2000, "a1"))
                .withMessageContaining("title");
    }

    @Test
    void create_authorNotFound_throws() {
        when(authorRepository.existsById("missing")).thenReturn(false);
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> bookService.create("Dune", "SciFi", 2000, "missing"));
    }

    @Test
    void create_futurePublishedYear_throws() {
        when(authorRepository.existsById("a1")).thenReturn(true);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> bookService.create("Dune", "SciFi", 9999, "a1"))
                .withMessageContaining("future");
    }
}
