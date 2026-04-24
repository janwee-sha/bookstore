package com.janwee.bookstore.book.presentation.rest;

import com.janwee.bookstore.book.core.domain.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthorResourceIntegrationTest extends RestApiIntegrationTestSupport {

    @Test
    void shouldReturnAuthorDetails() throws Exception {
        Author author = saveAuthor("Martin Fowler", "Refactoring", "13800000010");

        mockMvc.perform(get("/authors/{id}", author.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(author.id()))
                .andExpect(jsonPath("$.name").value("Martin Fowler"))
                .andExpect(jsonPath("$.profile").value("Refactoring"))
                .andExpect(jsonPath("$.phoneNumber").value("13800000010"));
    }

    @Test
    void shouldReturnNullBodyForMissingAuthor() throws Exception {
        mockMvc.perform(get("/authors/{id}", 9999L))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void shouldRegisterAuthor() throws Exception {
        String requestBody = """
                {
                  "name": "Vaughn Vernon",
                  "profile": "Implementing Domain-Driven Design",
                  "phoneNumber": "13800000011"
                }
                """;

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        assertEquals(1, authorRepo.count());
        Author saved = authorRepo.findAll().get(0);
        assertEquals("Vaughn Vernon", saved.name());
        assertEquals("Implementing Domain-Driven Design", saved.profile());
        assertEquals("13800000011", saved.phoneNumber());
    }

    @Test
    void shouldRejectRegisteringAuthorWhenNameIsBlank() throws Exception {
        String requestBody = """
                {
                  "name": "",
                  "profile": "blank name",
                  "phoneNumber": "13800000012"
                }
                """;

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detailedStatus").value(4005))
                .andExpect(jsonPath("$.detailedPhrase").value("BODY_CONSTRAINT_VIOLATION"))
                .andExpect(jsonPath("$.error.name").value("name required"));
    }
}
