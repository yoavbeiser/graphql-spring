package com.example.spring_for_graphql.controller;

import com.example.spring_for_graphql.model.Author;
import com.example.spring_for_graphql.service.AuthorService;
import com.example.spring_for_graphql.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@GraphQlTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired GraphQlTester graphQlTester;
    @MockitoBean AuthorService authorService;
    @MockitoBean BookService bookService;

    @Test
    void authors_returnsAll() {
        when(authorService.findAll()).thenReturn(List.of(
                new Author("a1", "Frank Herbert", "Sci-fi author")
        ));

        graphQlTester.document("{ authors { id name bio } }")
                .execute()
                .path("authors[0].name").entity(String.class).isEqualTo("Frank Herbert");
    }

    @Test
    void author_found() {
        when(authorService.findById("a1")).thenReturn(Optional.of(
                new Author("a1", "Frank Herbert", "Sci-fi author")
        ));

        graphQlTester.document("{ author(id: \"a1\") { id name bio } }")
                .execute()
                .path("author.name").entity(String.class).isEqualTo("Frank Herbert")
                .path("author.bio").entity(String.class).isEqualTo("Sci-fi author");
    }

    @Test
    void author_notFound_returnsNull() {
        when(authorService.findById("x")).thenReturn(Optional.empty());

        graphQlTester.document("{ author(id: \"x\") { name } }")
                .execute()
                .path("author").valueIsNull();
    }

    @Test
    void authorsByName_returnsMatches() {
        when(authorService.searchByName("frank")).thenReturn(List.of(
                new Author("a1", "Frank Herbert", "Sci-fi author")
        ));

        graphQlTester.document("{ authorsByName(name: \"frank\") { name } }")
                .execute()
                .path("authorsByName[0].name").entity(String.class).isEqualTo("Frank Herbert");
    }
}
