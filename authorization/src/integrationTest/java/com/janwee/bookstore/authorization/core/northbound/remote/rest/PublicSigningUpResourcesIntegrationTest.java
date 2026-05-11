package com.janwee.bookstore.authorization.core.northbound.remote.rest;

import com.janwee.bookstore.authorization.AuthorizationApplication;
import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.southbound.port.UserRepository;
import com.janwee.bookstore.authorization.core.southbound.adapter.jpa.SpringSecurityUserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = AuthorizationApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
class PublicSigningUpResourcesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SpringSecurityUserJpaRepository userJpaRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanDatabase() {
        userJpaRepo.deleteAll();
    }

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

    @Test
    void shouldRequireAuthenticationForUsersEndpoint() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().is3xxRedirection());
    }
}
