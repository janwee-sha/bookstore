package com.janwee.bookstore.order;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = OrderApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
class OrderApplicationStartupIntegrationTest {

    @Test
    void shouldStartApplicationContext() {
    }
}
