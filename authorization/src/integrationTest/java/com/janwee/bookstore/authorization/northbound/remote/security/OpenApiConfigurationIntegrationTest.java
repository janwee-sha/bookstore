package com.janwee.bookstore.authorization.northbound.remote.security;

import com.janwee.bookstore.authorization.AuthorizationApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = AuthorizationApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
class OpenApiConfigurationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExposeBearerJwtSecuritySchemeForProtectedOperations() throws Exception {
        mockMvc.perform(get("/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.type").value("http"))
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.scheme").value("bearer"))
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.bearerFormat").value("JWT"))
                .andExpect(jsonPath("$.paths['/users'].get.security[0].bearerAuth").isArray())
                .andExpect(jsonPath("$.paths['/users/{username}'].get.security[0].bearerAuth").isArray())
                .andExpect(jsonPath("$.paths['/public/sign-up'].post.security").doesNotExist());
    }
}
