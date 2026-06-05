package com.example.spring_for_graphql.controller;

import com.example.spring_for_graphql.model.Author;
import com.example.spring_for_graphql.model.Book;
import com.example.spring_for_graphql.service.AuthorService;
import com.example.spring_for_graphql.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    private final BookService bookService;

    @QueryMapping
    public List<Author> authors() {
        return authorService.findAll();
    }

    @QueryMapping
    public Author author(@Argument String id) {
        return authorService.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Author> authorsByName(@Argument String name) {
        return authorService.searchByName(name);
    }
}
