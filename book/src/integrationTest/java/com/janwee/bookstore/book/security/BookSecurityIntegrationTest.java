package com.janwee.bookstore.book.security;

import com.janwee.bookstore.book.BookApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = BookApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
class BookSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAllowAnonymousAccessToOpenApiDocs() throws Exception {
        mockMvc.perform(get("/api-docs"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldRejectBusinessResourcesWithoutBearerToken() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/authors"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowBusinessResourcesWithBearerToken() throws Exception {
        mockMvc.perform(get("/books").with(jwt()))
                .andExpect(status().isOk());
    }
}
