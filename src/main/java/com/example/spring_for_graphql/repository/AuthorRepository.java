package com.example.spring_for_graphql.repository;

import com.example.spring_for_graphql.model.Author;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AuthorRepository extends MongoRepository<Author, String> {

    boolean existsByNameIgnoreCase(String name);

    List<Author> findByNameContainingIgnoreCase(String name);
}
