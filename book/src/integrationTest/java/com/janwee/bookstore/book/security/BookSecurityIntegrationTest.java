package com.janwee.bookstore.book.security;

import com.janwee.bookstore.book.BookApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.type").value("http"))
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.scheme").value("bearer"))
                .andExpect(jsonPath("$.paths['/books'].get.security[0].bearerAuth").isArray())
                .andExpect(jsonPath("$.paths['/books'].post.security[0].bearerAuth").isArray());

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
    void shouldRejectBusinessResourcesWhenBearerTokenLacksRequiredAuthority() throws Exception {
        mockMvc.perform(get("/books").with(tokenWithAuthority("book:write")))
                .andExpect(status().isForbidden());

        String requestBody = """
                {
                  "name": "Kent Beck",
                  "profile": "TDD",
                  "phoneNumber": "13800000001"
                }
                """;

        mockMvc.perform(post("/authors")
                        .with(tokenWithAuthority("book:read"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowBusinessResourcesWithRequiredBearerAuthority() throws Exception {
        mockMvc.perform(get("/books").with(tokenWithAuthority("book:read")))
                .andExpect(status().isOk());
    }

    private RequestPostProcessor tokenWithAuthority(String authority) {
        return jwt().authorities(new SimpleGrantedAuthority(authority));
    }
}
