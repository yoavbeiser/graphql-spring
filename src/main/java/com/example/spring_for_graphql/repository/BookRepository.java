package com.example.spring_for_graphql.repository;

import com.example.spring_for_graphql.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {

    List<Book> findByGenre(String genre);

    List<Book> findByAuthorIdIn(List<String> authorIds);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByPublishedYearBetween(int from, int to);
}
