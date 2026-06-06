package com.example.spring_for_graphql;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.example.spring_for_graphql.repository.AuthorRepository;
import com.example.spring_for_graphql.repository.BookRepository;
import com.example.spring_for_graphql.repository.DataSeeder;

@SpringBootTest
class SpringForGraphqlApplicationTests {

	@MockitoBean BookRepository bookRepository;
	@MockitoBean AuthorRepository authorRepository;
	@MockitoBean DataSeeder dataSeeder;

	@Test
	void contextLoads() {
	}

}
