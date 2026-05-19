package com.janwee.bookstore.gateway;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = GatewayApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        properties = {
                "spring.config.import=",
                "spring.cloud.config.enabled=false",
                "spring.cloud.discovery.enabled=false",
                "eureka.client.enabled=false",
                "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:7030/oauth2/jwks"
        }
)
@AutoConfigureMockMvc
class GatewaySecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRejectGatewayRequestsWithoutBearerToken() throws Exception {
        mockMvc.perform(get("/book/books"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowFrontendCorsPreflight() throws Exception {
        mockMvc.perform(options("/book/books")
                        .header("Origin", "http://127.0.0.1:8088")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Access-Control-Request-Headers", "Authorization"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://127.0.0.1:8088"));
    }

    @Test
    void shouldPassAuthenticatedRequestsToGatewayRoutingLayer() throws Exception {
        ServletException exception = assertThrows(ServletException.class,
                () -> mockMvc.perform(get("/book/books").with(jwt())));

        assertTrue(exception.getMessage().contains("Unable to find instance for book"));
    }
}
