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
                "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:18083/oauth2/jwks"
        }
)
class GatewayApplicationStartupIntegrationTest {

    @Test
    void shouldStartApplicationContext() {
    }
}
