package com.example.spring_for_graphql.restControllers;

import com.example.spring_for_graphql.exception.ResourceNotFoundException;
import com.example.spring_for_graphql.model.Author;
import com.example.spring_for_graphql.model.Book;
import com.example.spring_for_graphql.service.AuthorService;
import com.example.spring_for_graphql.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorService authorService;
    private final BookService bookService;

    @GetMapping
    public List<Author> authors() {
        return authorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> author(@PathVariable String id) {
        return authorService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found: " + id));
    }

    @GetMapping("/search")
    public List<Author> authorsByName(@RequestParam String name) {
        return authorService.searchByName(name);
    }
}
