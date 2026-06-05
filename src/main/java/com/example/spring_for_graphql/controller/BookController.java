package com.example.spring_for_graphql.controller;

import com.example.spring_for_graphql.model.Author;
import com.example.spring_for_graphql.model.Book;
import com.example.spring_for_graphql.service.AuthorService;
import com.example.spring_for_graphql.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @QueryMapping
    public List<Book> books() {
        return bookService.findAll();
    }

    @QueryMapping
    public Book book(@Argument String id) {
        return bookService.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Book> booksByGenre(@Argument String genre) {
        return bookService.findByGenre(genre);
    }

    @QueryMapping
    public List<Book> booksByTitle(@Argument String title) {
        return bookService.searchByTitle(title);
    }

    @QueryMapping
    public List<Book> booksByYearRange(@Argument int from, @Argument int to) {
        return bookService.findByYearRange(from, to);
    }
}
