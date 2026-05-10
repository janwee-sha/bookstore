package com.janwee.bookstore.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = ConfigApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
class ConfigApplicationStartupIntegrationTest {

    @Test
    void shouldStartApplicationContext() {
    }
}
