package com.janwee.bookstore.book.interfaces.rest;

import com.janwee.bookstore.book.domain.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthorResourceSecurityIntegrationTest extends RestApiIntegrationTestSupport {
    @Test
    void authorOfIdShouldAllowOnlyBookReadWriteAccess() throws Exception {
        Author author = saveAuthor("Martin Fowler", "Refactoring", "13800000010");

        mockMvc.perform(get("/authors/{id}", author.id()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/authors/{id}", author.id()).with(bookReader()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/authors/{id}", author.id()).with(bookWriter()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/authors/{id}", author.id()).with(orderWriter()))
                .andExpect(status().isForbidden());
    }

    @Test
    void registerShouldAllowOnlyBookWriteAccess() throws Exception {
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
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(bookReader()))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(bookWriter()))
                .andExpect(status().isOk());
        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(orderWriter()))
                .andExpect(status().isForbidden());
    }
}
