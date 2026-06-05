# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./gradlew build

# Run the application (requires MongoDB on localhost:27017)
./gradlew bootRun

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.example.spring_for_graphql.SpringForGraphqlApplicationTests"

# Run a single test method
./gradlew test --tests "com.example.spring_for_graphql.SpringForGraphqlApplicationTests.contextLoads"

# Clean build
./gradlew clean build
```

## Stack

- **Spring Boot 4.0.6** with Java 17
- **Spring for GraphQL** (`spring-boot-starter-graphql`) — GraphQL schema files live in `src/main/resources/graphql/` (`.graphqls` extension)
- **Spring Web** (`spring-boot-starter-web`) — servlet stack; GraphQL is served at `/graphql`, GraphiQL UI at `/graphiql`
- **Spring Data MongoDB** (`spring-boot-starter-data-mongodb`) — MongoDB at `mongodb://localhost:27017/springgraphql`
- **Testing**: `spring-boot-starter-graphql-test` provides `GraphQlTester` and `@GraphQlTest`

## Architecture

```
src/main/java/com/example/spring_for_graphql/
  model/          — MongoDB @Document classes (Book, Author)
  repository/     — MongoRepository interfaces
  service/        — Business logic; BookService holds the Reactor Sinks for subscriptions
  controller/     — @Controller classes wired to GraphQL operations
src/main/resources/
  graphql/schema.graphqls  — single schema file defining all types
  application.properties   — MongoDB URI + GraphiQL toggle
```

### Key annotation patterns (the main demo points)

| Annotation | Where used | What it shows |
|---|---|---|
| `@QueryMapping` | `BookController`, `AuthorController` | Maps a method to a GraphQL query by method name |
| `@MutationMapping` | `BookController`, `AuthorController` | Maps a method to a GraphQL mutation |
| `@SubscriptionMapping` | `BookController.bookAdded()` | Returns `Flux<Book>`; delivered via SSE |
| `@SchemaMapping(typeName="Book")` | `BookController.author()` | Resolves a nested field (Book → Author) |
| `@BatchMapping(typeName="Author")` | `AuthorController.books()` | Batch-loads Author → Books in one query; solves N+1 |
| `@Argument` | method parameters | Binds a GraphQL argument to a Java parameter or record |

Input types (`BookInput`, `AuthorInput`) are Java records defined as inner classes in the respective controller.

The `bookAdded` subscription uses a `Sinks.Many<Book>` in `BookService`; every `createBook` mutation emits to it. Subscriptions are delivered over HTTP SSE — no WebSocket config needed.
