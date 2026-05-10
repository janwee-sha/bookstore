package com.janwee.bookstore.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = GatewayApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        properties = {
                "spring.config.import=",
                "spring.cloud.config.enabled=false",
                "spring.cloud.discovery.enabled=false",
                "eureka.client.enabled=false",
                "spring.security.oauth2.client.registration.github.client-id=test-github-client",
                "spring.security.oauth2.client.registration.github.client-secret=test-github-secret",
                "spring.security.oauth2.client.registration.bookstore.client-secret=test-bookstore-secret",
                "spring.security.oauth2.client.registration.bookstore.redirect-uri=http://127.0.0.1:7001/authorized",
                "spring.security.oauth2.client.provider.spring.authorization-uri=http://localhost:7030/oauth2/authorize",
                "spring.security.oauth2.client.provider.spring.token-uri=http://localhost:7030/oauth2/token",
                "spring.security.oauth2.client.provider.spring.jwk-set-uri=http://localhost:7030/oauth2/jwks"
        }
)
class GatewayApplicationStartupIntegrationTest {

    @Test
    void shouldStartApplicationContext() {
    }
}
