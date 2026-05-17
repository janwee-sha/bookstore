package com.janwee.bookstore.authorization.core.northbound.remote.rest;

import com.janwee.bookstore.authorization.AuthorizationApplication;
import com.janwee.bookstore.authorization.core.domain.Role;
import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.southbound.adapter.jpa.SpringSecurityUserJpaRepository;
import com.janwee.bookstore.authorization.core.southbound.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = AuthorizationApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
class UserResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringSecurityUserJpaRepository userJpaRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtAuthenticationConverter jwtAuthenticationConverter;

    @BeforeEach
    void cleanDatabase() {
        userJpaRepo.deleteAll();
    }

    @Test
    void shouldReturnUnauthorizedForUnauthenticatedUsersEndpoint() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUsersForJwtWithUserReadScope() throws Exception {
        userRepo.save(new User.Builder()
                .email("reader@bookstore.com")
                .password("P@ssw0rd!")
                .role(Role.USER)
                .build());

        mockMvc.perform(get("/users")
                        .with(tokenWithScope("user:read")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("reader@bookstore.com"));
    }

    @Test
    void shouldReturnNotFoundForMissingUser() throws Exception {
        mockMvc.perform(get("/users/missing@bookstore.com")
                        .with(tokenWithScope("user:read")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No such user of username: missing@bookstore.com"))
                .andExpect(jsonPath("$.path").value("/users/missing@bookstore.com"));
    }

    @Test
    void shouldReturnForbiddenForJwtWithoutUserReadScope() throws Exception {
        mockMvc.perform(get("/users")
                        .with(tokenWithScope("user:write")))
                .andExpect(status().isForbidden());
    }

    private RequestPostProcessor tokenWithScope(String scope) {
        return jwt()
                .jwt(token -> token.claim("scope", List.of(scope)))
                .authorities(token -> jwtAuthenticationConverter.convert(token).getAuthorities());
    }
}
