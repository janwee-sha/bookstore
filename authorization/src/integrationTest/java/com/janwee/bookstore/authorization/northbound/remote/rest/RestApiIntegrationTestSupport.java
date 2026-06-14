package com.janwee.bookstore.authorization.northbound.remote.rest;

import com.janwee.bookstore.authorization.AuthorizationApplication;
import com.janwee.bookstore.authorization.domain.Role;
import com.janwee.bookstore.authorization.domain.User;
import com.janwee.bookstore.authorization.southbound.adapter.jpa.SpringSecurityUserJpaRepository;
import com.janwee.bookstore.authorization.southbound.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest(
        classes = AuthorizationApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
abstract class RestApiIntegrationTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserRepository userRepo;

    @Autowired
    private SpringSecurityUserJpaRepository userJpaRepo;

    @Autowired
    private JwtAuthenticationConverter jwtAuthenticationConverter;

    @BeforeEach
    void cleanDatabase() {
        userJpaRepo.deleteAll();
    }

    protected RequestPostProcessor userReader() {
        return tokenWithScope("user:read");
    }

    protected RequestPostProcessor userWriter() {
        return tokenWithScope("user:write");
    }

    protected RequestPostProcessor orderWriter() {
        return tokenWithScope("order:write");
    }

    protected RequestPostProcessor tokenWithScope(String scope) {
        return jwt()
                .jwt(token -> token.claim("scope", List.of(scope)))
                .authorities(token -> jwtAuthenticationConverter.convert(token).getAuthorities());
    }

    protected User saveUser(String email, String password, Role role) {
        return userRepo.save(new User.Builder()
                .email(email)
                .password(password)
                .role(role)
                .build());
    }
}
