package com.example.spring_for_graphql.repository;

import com.example.spring_for_graphql.model.Author;
import com.example.spring_for_graphql.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (authorRepository.count() > 0) {
            return;
        }

        Author orwell  = authorRepository.save(new Author("George Orwell",  "English novelist and essayist, known for dystopian fiction."));
        Author tolkien = authorRepository.save(new Author("J.R.R. Tolkien", "English author and philologist, creator of Middle-earth."));
        Author herbert = authorRepository.save(new Author("Frank Herbert",  "American science fiction author, best known for the Dune saga."));

        bookRepository.saveAll(List.of(
            new Book("1984",                      "Dystopian",       1949, orwell.getId()),
            new Book("Animal Farm",               "Political",       1945, orwell.getId()),
            new Book("The Fellowship of the Ring","Fantasy",         1954, tolkien.getId()),
            new Book("The Two Towers",            "Fantasy",         1954, tolkien.getId()),
            new Book("The Return of the King",    "Fantasy",         1955, tolkien.getId()),
            new Book("Dune",                      "Science Fiction", 1965, herbert.getId()),
            new Book("Dune Messiah",              "Science Fiction", 1969, herbert.getId())
        ));
    }
}
