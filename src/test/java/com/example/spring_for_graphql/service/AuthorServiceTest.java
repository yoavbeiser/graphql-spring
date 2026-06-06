package com.example.spring_for_graphql.service;

import com.example.spring_for_graphql.model.Author;
import com.example.spring_for_graphql.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock AuthorRepository authorRepository;
    @InjectMocks AuthorService authorService;

    @Test
    void findAll_returnsList() {
        var author = new Author("a1", "Frank Herbert", "Sci-fi author");
        when(authorRepository.findAll()).thenReturn(List.of(author));
        assertThat(authorService.findAll()).containsExactly(author);
    }

    @Test
    void findById_found() {
        var author = new Author("a1", "Frank Herbert", "Sci-fi author");
        when(authorRepository.findById("a1")).thenReturn(Optional.of(author));
        assertThat(authorService.findById("a1")).contains(author);
    }

    @Test
    void findById_notFound() {
        when(authorRepository.findById("x")).thenReturn(Optional.empty());
        assertThat(authorService.findById("x")).isEmpty();
    }

    @Test
    void searchByName_returnsMatches() {
        var author = new Author("a1", "Frank Herbert", "Sci-fi author");
        when(authorRepository.findByNameContainingIgnoreCase("frank")).thenReturn(List.of(author));
        assertThat(authorService.searchByName("frank")).containsExactly(author);
    }

    @Test
    void create_success() {
        when(authorRepository.existsByNameIgnoreCase("Frank Herbert")).thenReturn(false);
        var saved = new Author("a1", "Frank Herbert", "Sci-fi author");
        when(authorRepository.save(any(Author.class))).thenReturn(saved);
        assertThat(authorService.create("Frank Herbert", "Sci-fi author")).isEqualTo(saved);
    }

    @Test
    void create_blankName_throws() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> authorService.create("  ", "Bio"))
                .withMessageContaining("name");
    }

    @Test
    void create_duplicateName_throws() {
        when(authorRepository.existsByNameIgnoreCase("Frank Herbert")).thenReturn(true);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> authorService.create("Frank Herbert", "Bio"))
                .withMessageContaining("already exists");
    }
}
