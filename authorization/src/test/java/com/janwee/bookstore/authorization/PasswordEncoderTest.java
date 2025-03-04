package com.janwee.bookstore.authorization;

import com.janwee.bookstore.authorization.infrastructure.security.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = SecurityConfiguration.class)
public class PasswordEncoderTest {
    @Autowired
    private PasswordEncoder encoder;

    @Test
    void givenSamePassShouldMatchesEncrypted() {
        String encrypted = encoder.encode("pass");
        System.out.println(encrypted);
        assertTrue(encoder.matches("pass", encrypted));
    }
}
