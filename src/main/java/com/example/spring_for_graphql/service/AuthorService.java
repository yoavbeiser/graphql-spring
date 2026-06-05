package com.example.spring_for_graphql.service;

import com.example.spring_for_graphql.model.Author;
import com.example.spring_for_graphql.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public Optional<Author> findById(String id) {
        return authorRepository.findById(id);
    }

    public List<Author> findAllById(List<String> ids) {
        return authorRepository.findAllById(ids);
    }

    public List<Author> searchByName(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }

    public Author create(String name, String bio) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Author name must not be blank");
        }
        if (authorRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("Author with name '" + name + "' already exists");
        }
        return authorRepository.save(new Author(name.strip(), bio));
    }
}
