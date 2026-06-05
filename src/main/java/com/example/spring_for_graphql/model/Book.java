package com.example.spring_for_graphql.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    private String id;
    private String title;
    private String genre;
    private Integer publishedYear;
    private String authorId;

    public Book(String title, String genre, Integer publishedYear, String authorId) {
        this.title = title;
        this.genre = genre;
        this.publishedYear = publishedYear;
        this.authorId = authorId;
    }
}
