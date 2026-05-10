package com.janwee.bookstore.eureka;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = EurekaApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.config.import=",
                "spring.cloud.config.enabled=false",
                "eureka.instance.hostname=localhost",
                "eureka.client.register-with-eureka=false",
                "eureka.client.fetch-registry=false"
        }
)
class EurekaApplicationStartupIntegrationTest {

    @Test
    void shouldStartApplicationContext() {
    }
}
