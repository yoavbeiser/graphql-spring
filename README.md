# Spring for GraphQL — Demo

A presentation project demonstrating the core features of the [Spring for GraphQL](https://docs.spring.io/spring-graphql/reference/) library using a Books & Authors domain backed by MongoDB.

## Prerequisites

- Java 17+
- MongoDB running on `localhost:27017`

```bash
# Start MongoDB with Docker if needed
docker run -d -p 27017:27017 --name mongo mongo:latest
```

## Running

```bash
./gradlew bootRun
```

Open **GraphiQL** at [http://localhost:8080/graphiql](http://localhost:8080/graphiql)

---

## Features Demonstrated

### 1. `@QueryMapping` — Basic Queries

Maps controller methods to GraphQL queries by method name.

```graphql
query {
  books {
    id title genre publishedYear
  }
}

query {
  book(id: "...") {
    title
  }
}

query {
  booksByGenre(genre: "Fiction") {
    title
  }
}
```

### 2. `@MutationMapping` — Mutations

```graphql
mutation {
  createAuthor(input: { name: "George Orwell", bio: "English novelist" }) {
    id name
  }
}

mutation {
  createBook(input: {
    title: "1984"
    genre: "Dystopian"
    publishedYear: 1949
    authorId: "<author-id>"
  }) {
    id title
  }
}

mutation {
  deleteBook(id: "<book-id>")
}
```

### 3. `@SchemaMapping` — Nested Field Resolution

Resolves `Book.author` by fetching the author for each individual book. The method receives the parent `Book` as its first argument.

```graphql
query {
  books {
    title
    author {
      name bio
    }
  }
}
```

> **Note**: This triggers N+1 queries when fetching many books. See `@BatchMapping` below for the solution.

### 4. `@BatchMapping` — Solving N+1

`AuthorController.books()` resolves `Author.books` for a **list** of authors in a **single** MongoDB query, then distributes results back via a `Map<Author, List<Book>>`.

```graphql
query {
  authors {
    name
    books {       # resolved with one DB call for all authors
      title genre
    }
  }
}
```

### 5. `@SubscriptionMapping` — Real-time Updates

Returns a `Flux<Book>` backed by a `Sinks.Many`. Delivered via **Server-Sent Events** (SSE) — no extra config needed with the servlet stack.

Open two GraphiQL tabs:

**Tab 1 — subscribe:**
```graphql
subscription {
  bookAdded {
    id title genre
  }
}
```

**Tab 2 — trigger an event:**
```graphql
mutation {
  createBook(input: {
    title: "Animal Farm"
    genre: "Satire"
    publishedYear: 1945
    authorId: "<author-id>"
  }) {
    id title
  }
}
```

---

## Project Structure

```
src/main/
├── java/com/example/spring_for_graphql/
│   ├── model/          Book.java, Author.java  (@Document)
│   ├── repository/     BookRepository.java, AuthorRepository.java
│   ├── service/        BookService.java (+ Sinks), AuthorService.java
│   └── controller/     BookController.java, AuthorController.java
└── resources/
    ├── graphql/schema.graphqls
    └── application.properties
```
