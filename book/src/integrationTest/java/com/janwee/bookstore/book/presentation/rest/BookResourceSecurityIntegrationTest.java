package com.janwee.bookstore.book.presentation.rest;

import com.janwee.bookstore.book.domain.model.Author;
import com.janwee.bookstore.book.domain.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookResourceSecurityIntegrationTest extends RestApiIntegrationTestSupport {
    @Test
    void booksShouldAllowOnlyBookReadWriteAccess() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/books").with(bookReader()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/books").with(bookWriter()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/books").with(orderWriter()))
                .andExpect(status().isForbidden());
    }

    @Test
    void bookShouldAllowOnlyBookReadWriteAccess() throws Exception {
        Book book = saveBook();

        mockMvc.perform(get("/books/{id}", book.id()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/books/{id}", book.id()).with(bookReader()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/books/{id}", book.id()).with(bookWriter()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/books/{id}", book.id()).with(orderWriter()))
                .andExpect(status().isForbidden());
    }

    @Test
    void hasBookOfIdShouldAllowOnlyBookReadWriteAccess() throws Exception {
        Book book = saveBook();

        mockMvc.perform(head("/books/{id}", book.id()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(head("/books/{id}", book.id()).with(bookReader()))
                .andExpect(status().isOk());
        mockMvc.perform(head("/books/{id}", book.id()).with(bookWriter()))
                .andExpect(status().isOk());
        mockMvc.perform(head("/books/{id}", book.id()).with(orderWriter()))
                .andExpect(status().isForbidden());
    }

    @Test
    void publishShouldAllowOnlyBookWriteAccess() throws Exception {
        Author author = saveAuthor("Neal Ford", "Architecture", "13800000021");
        String requestBody = """
                {
                  "name": "Building Evolutionary Architectures",
                  "price": {
                    "currency": "USD",
                    "amount": 66.60
                  },
                  "amount": 12,
                  "publishedAt": "2020-01-01",
                  "publisher": "O'Reilly",
                  "authorId": %d
                }
                """.formatted(author.id());

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(bookReader()))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(bookWriter()))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(orderWriter()))
                .andExpect(status().isForbidden());
    }

    @Test
    void changeShouldAllowOnlyBookWriteAccess() throws Exception {
        Book book = saveBook();
        String requestBody = """
                {
                  "amount": 3
                }
                """;

        mockMvc.perform(patch("/books/{id}", book.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(patch("/books/{id}", book.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(bookReader()))
                .andExpect(status().isForbidden());
        mockMvc.perform(patch("/books/{id}", book.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(bookWriter()))
                .andExpect(status().isOk());
        mockMvc.perform(patch("/books/{id}", book.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(orderWriter()))
                .andExpect(status().isForbidden());
    }

    @Test
    void withdrawShouldAllowOnlyBookWriteAccess() throws Exception {
        Book book = saveBook();

        mockMvc.perform(delete("/books/{id}", book.id()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/books/{id}", book.id()).with(bookReader()))
                .andExpect(status().isForbidden());
        mockMvc.perform(delete("/books/{id}", book.id()).with(orderWriter()))
                .andExpect(status().isForbidden());
        mockMvc.perform(delete("/books/{id}", book.id()).with(bookWriter()))
                .andExpect(status().isOk());
    }

    private Book saveBook() {
        Author author = saveAuthor("Eric Evans", "DDD", "13800000020");
        return saveBook("Domain-Driven Design", 9, new BigDecimal("88.80"),
                LocalDate.of(2018, 9, 1), "Pearson", author.id());
    }
}
