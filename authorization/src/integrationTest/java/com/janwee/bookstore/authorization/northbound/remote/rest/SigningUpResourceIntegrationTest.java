package com.janwee.bookstore.authorization.northbound.remote.rest;

import com.janwee.bookstore.authorization.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SigningUpResourceIntegrationTest extends RestApiIntegrationTestSupport {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldCreateUserFromPublicSignUp() throws Exception {
        String requestBody = """
                {
                  "email": "signup@bookstore.com",
                  "password": "P@ssw0rd!"
                }
                """;

        mockMvc.perform(post("/public/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        User saved = userRepo.userOfEmail("signup@bookstore.com").orElseThrow();
        assertAll(
                () -> assertEquals("signup@bookstore.com", saved.email()),
                () -> assertNotEquals("P@ssw0rd!", saved.password()),
                () -> assertTrue(passwordEncoder.matches("P@ssw0rd!", saved.password()))
        );
    }

    @Test
    void shouldRejectInvalidPublicSignUpBody() throws Exception {
        String requestBody = """
                {
                  "email": "invalid-email",
                  "password": ""
                }
                """;

        mockMvc.perform(post("/public/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
