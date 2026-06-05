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

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @GetMapping
    public List<Book> getAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable String id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id));
    }

    @GetMapping("/genre/{genre}")
    public List<Book> getByGenre(@PathVariable String genre) {
        return bookService.findByGenre(genre);
    }

    @GetMapping("/search")
    public List<Book> search(@RequestParam String title) {
        return bookService.searchByTitle(title);
    }

    @GetMapping("/year")
    public List<Book> getByYearRange(@RequestParam int from, @RequestParam int to) {
        return bookService.findByYearRange(from, to);
    }
}
