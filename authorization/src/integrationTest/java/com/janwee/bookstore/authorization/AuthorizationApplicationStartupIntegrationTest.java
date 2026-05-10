package com.janwee.bookstore.authorization;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = AuthorizationApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
class AuthorizationApplicationStartupIntegrationTest {

    @Test
    void shouldStartApplicationContext() {
    }
}
