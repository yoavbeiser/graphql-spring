package com.example.spring_for_graphql.controller;

import com.example.spring_for_graphql.model.Book;
import com.example.spring_for_graphql.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@GraphQlTest(BookController.class)
class BookControllerTest {

    @Autowired GraphQlTester graphQlTester;
    @MockitoBean BookService bookService;

    @Test
    void books_returnsAll() {
        when(bookService.findAll()).thenReturn(List.of(
                new Book("b1", "Dune", "SciFi", 1965, "a1")
        ));

        graphQlTester.document("{ books { id title genre publishedYear } }")
                .execute()
                .path("books[0].title").entity(String.class).isEqualTo("Dune");
    }

    @Test
    void book_found() {
        when(bookService.findById("b1")).thenReturn(Optional.of(
                new Book("b1", "Dune", "SciFi", 1965, "a1")
        ));

        graphQlTester.document("{ book(id: \"b1\") { id title genre publishedYear } }")
                .execute()
                .path("book.title").entity(String.class).isEqualTo("Dune")
                .path("book.publishedYear").entity(Integer.class).isEqualTo(1965);
    }

    @Test
    void book_notFound_returnsNull() {
        when(bookService.findById("x")).thenReturn(Optional.empty());

        graphQlTester.document("{ book(id: \"x\") { id title } }")
                .execute()
                .path("book").valueIsNull();
    }

    @Test
    void booksByGenre_returnsFiltered() {
        when(bookService.findByGenre("SciFi")).thenReturn(List.of(
                new Book("b1", "Dune", "SciFi", 1965, "a1")
        ));

        graphQlTester.document("{ booksByGenre(genre: \"SciFi\") { title genre } }")
                .execute()
                .path("booksByGenre[0].genre").entity(String.class).isEqualTo("SciFi");
    }

    @Test
    void booksByTitle_returnsMatches() {
        when(bookService.searchByTitle("Dune")).thenReturn(List.of(
                new Book("b1", "Dune", "SciFi", 1965, "a1"),
                new Book("b2", "Dune Messiah", "SciFi", 1969, "a1")
        ));

        graphQlTester.document("{ booksByTitle(title: \"Dune\") { title } }")
                .execute()
                .path("booksByTitle").entityList(Book.class).hasSize(2);
    }

    @Test
    void booksByYearRange_returnsFiltered() {
        when(bookService.findByYearRange(1960, 1970)).thenReturn(List.of(
                new Book("b1", "Dune", "SciFi", 1965, "a1")
        ));

        graphQlTester.document("{ booksByYearRange(from: 1960, to: 1970) { title publishedYear } }")
                .execute()
                .path("booksByYearRange[0].publishedYear").entity(Integer.class).isEqualTo(1965);
    }

    @Test
    void booksByYearRange_invalidRange_returnsError() {
        when(bookService.findByYearRange(2020, 2000))
                .thenThrow(new IllegalArgumentException("'from' year must not be greater than 'to' year"));

        graphQlTester.document("{ booksByYearRange(from: 2020, to: 2000) { title } }")
                .execute()
                .errors()
                .satisfy(errors -> org.assertj.core.api.Assertions.assertThat(errors).isNotEmpty());
    }
}
