package com.janwee.bookstore.book;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = BookApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
class BookApplicationStartupIntegrationTest {

    @Test
    void shouldStartApplicationContext() {
    }
}
