package com.example.spring_for_graphql.service;

import com.example.spring_for_graphql.exception.ResourceNotFoundException;
import com.example.spring_for_graphql.model.Book;
import com.example.spring_for_graphql.repository.AuthorRepository;
import com.example.spring_for_graphql.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    public List<Book> findByGenre(String genre) {
        return bookRepository.findByGenre(genre);
    }

    public List<Book> findByAuthorIdIn(List<String> authorIds) {
        return bookRepository.findByAuthorIdIn(authorIds);
    }

    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> findByYearRange(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("'from' year must not be greater than 'to' year");
        }
        return bookRepository.findByPublishedYearBetween(from, to);
    }

    public Book create(String title, String genre, Integer publishedYear, String authorId) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Book title must not be blank");
        }
        if (!authorRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("Author not found: " + authorId);
        }
        if (publishedYear != null && publishedYear > Year.now().getValue()) {
            throw new IllegalArgumentException("Published year cannot be in the future");
        }
        return bookRepository.save(new Book(title.strip(), genre, publishedYear, authorId));
    }
}
