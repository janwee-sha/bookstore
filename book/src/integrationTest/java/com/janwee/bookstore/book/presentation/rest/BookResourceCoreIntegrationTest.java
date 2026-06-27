package com.janwee.bookstore.book.presentation.rest;

import com.janwee.bookstore.book.domain.model.Author;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.infrastructure.persistence.entity.BookPO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookResourceCoreIntegrationTest extends RestApiIntegrationTestSupport {

    @Test
    void shouldReturnBooksPageWithEmbeddedAuthors() throws Exception {
        Author firstAuthor = saveAuthor("Kent Beck", "TDD", "13800000001");
        Author secondAuthor = saveAuthor("Martin Fowler", "Refactoring", "13800000002");
        saveBook("Test-Driven Development", 5, new BigDecimal("42.50"),
                LocalDate.of(2020, 1, 1), "Addison-Wesley", firstAuthor.id());
        saveBook("Refactoring", 8, new BigDecimal("56.00"),
                LocalDate.of(2024, 5, 1), "Addison-Wesley", secondAuthor.id());

        mockMvc.perform(get("/books").with(bookReader()).param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("Refactoring"))
                .andExpect(jsonPath("$.content[0].price.currency").value("USD"))
                .andExpect(jsonPath("$.content[0].price.amount").value(56.00))
                .andExpect(jsonPath("$.content[0].author.name").value("Martin Fowler"))
                .andExpect(jsonPath("$.content[1].name").value("Test-Driven Development"))
                .andExpect(jsonPath("$.content[1].author.name").value("Kent Beck"))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void shouldReturnBookDetails() throws Exception {
        Author author = saveAuthor("Eric Evans", "DDD", "13800000003");
        Book book = saveBook("Domain-Driven Design", 9, new BigDecimal("88.80"),
                LocalDate.of(2018, 9, 1), "Pearson", author.id());

        mockMvc.perform(get("/books/{id}", book.id()).with(bookReader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.id()))
                .andExpect(jsonPath("$.name").value("Domain-Driven Design"))
                .andExpect(jsonPath("$.publisher").value("Pearson"))
                .andExpect(jsonPath("$.price.currency").value("USD"))
                .andExpect(jsonPath("$.price.amount").value(88.80))
                .andExpect(jsonPath("$.author.id").value(author.id()))
                .andExpect(jsonPath("$.author.name").value("Eric Evans"));
    }

    @Test
    void shouldReturnNotFoundForMissingBookDetails() throws Exception {
        mockMvc.perform(get("/books/{id}", 9999L).with(bookReader()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("No such book of ID: 9999"));
    }

    @Test
    void shouldCheckBookExistenceByHeadRequest() throws Exception {
        Author author = saveAuthor("Robert C. Martin", "Clean Code", "13800000004");
        Book book = saveBook("Clean Code", 7, new BigDecimal("49.90"),
                LocalDate.of(2019, 4, 1), "Prentice Hall", author.id());

        mockMvc.perform(head("/books/{id}", book.id()).with(bookReader()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        mockMvc.perform(head("/books/{id}", 9999L).with(bookReader()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPublishBook() throws Exception {
        Author author = saveAuthor("Neal Ford", "Architecture", "13800000005");
        String requestBody = """
                {
                  "name": "Building Evolutionary Architectures",
                  "price": {
                    "currency": "USD",
                    "amount": 66.60
                  },
                  "amount": 12,
                  "publisher": "O'Reilly",
                  "authorId": %d
                }
                """.formatted(author.id());

        mockMvc.perform(post("/books")
                        .with(bookWriter())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        assertEquals(1, bookRepo.count());
        BookPO saved = bookRepo.findAll().get(0);
        assertEquals("Building Evolutionary Architectures", saved.getName());
        assertEquals(12, saved.getAmount());
        assertEquals("O'Reilly", saved.getPublisher());
        assertEquals(author.id(), saved.getAuthorId());
    }

    @Test
    void shouldRejectPublishingBookWhenBodyIsInvalid() throws Exception {
        Author author = saveAuthor("Vaughn Vernon", "DDD", "13800000006");
        String requestBody = """
                {
                  "name": "",
                  "price": {
                    "currency": "USD",
                    "amount": 55.50
                  },
                  "amount": -1,
                  "publisher": "Addison-Wesley",
                  "authorId": %d
                }
                """.formatted(author.id());

        mockMvc.perform(post("/books")
                        .with(bookWriter())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detailedStatus").value(4005))
                .andExpect(jsonPath("$.detailedPhrase").value("BODY_CONSTRAINT_VIOLATION"))
                .andExpect(jsonPath("$.error.name").value("Name is required"))
                .andExpect(jsonPath("$.error.amount").value("Amount should not be negative"));
    }

    @Test
    void shouldRejectPublishingBookWhenAuthorDoesNotExist() throws Exception {
        String requestBody = """
                {
                  "name": "Patterns of Enterprise Application Architecture",
                  "price": {
                    "currency": "USD",
                    "amount": 77.70
                  },
                  "amount": 6,
                  "publisher": "Addison-Wesley",
                  "authorId": 9999
                }
                """;

        mockMvc.perform(post("/books")
                        .with(bookWriter())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detailedStatus").value(4000))
                .andExpect(jsonPath("$.detailedPhrase").value("BAD_DOMAIN_REQUEST"))
                .andExpect(jsonPath("$.error").value("No such author"));
    }

    @Test
    void shouldChangeBook() throws Exception {
        Author originalAuthor = saveAuthor("Sam Newman", "Microservices", "13800000007");
        Author newAuthor = saveAuthor("Martin Kleppmann", "Data", "13800000008");
        Book book = saveBook("Monolith to Microservices", 10, new BigDecimal("58.80"),
                LocalDate.of(2020, 8, 1), "O'Reilly", originalAuthor.id());
        String requestBody = """
                {
                  "amount": 3,
                  "publisher": "Manning",
                  "authorId": %d
                }
                """.formatted(newAuthor.id());

        mockMvc.perform(patch("/books/{id}", book.id())
                        .with(bookWriter())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        BookPO changed = bookRepo.findById(book.id()).orElseThrow();
        assertEquals(3, changed.getAmount());
        assertEquals("Manning", changed.getPublisher());
        assertEquals(newAuthor.id(), changed.getAuthorId());
    }

    @Test
    void shouldReturnNotFoundWhenChangingMissingBook() throws Exception {
        String requestBody = """
                {
                  "amount": 3
                }
                """;

        mockMvc.perform(patch("/books/{id}", 9999L)
                        .with(bookWriter())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("No such book of ID: 9999"));
    }

    @Test
    void shouldWithdrawBook() throws Exception {
        Author author = saveAuthor("Craig Walls", "Spring", "13800000009");
        Book book = saveBook("Spring in Action", 15, new BigDecimal("62.30"),
                LocalDate.of(2021, 1, 1), "Manning", author.id());

        mockMvc.perform(delete("/books/{id}", book.id()).with(bookWriter()))
                .andExpect(status().isOk());

        assertFalse(bookRepo.existsById(book.id()));
    }

    @Test
    void shouldIgnoreDeletingMissingBook() throws Exception {
        mockMvc.perform(delete("/books/{id}", 9999L).with(bookWriter()))
                .andExpect(status().isOk());
    }
}
