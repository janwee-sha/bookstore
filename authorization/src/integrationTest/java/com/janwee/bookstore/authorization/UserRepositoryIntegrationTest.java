package com.janwee.bookstore.authorization;

import com.janwee.bookstore.authorization.core.domain.Role;
import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.domain.UserRepository;
import com.janwee.bookstore.authorization.core.infrastructure.persistence.SecurityBasedUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = AuthorizationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserRepositoryIntegrationTest {
    @Autowired
    private UserRepository userRepo;

    @Test
    void testSavingUser() {
        User user1 = new SecurityBasedUser()
                .identifiedBy("pass1")
                .ofRole(Role.USER)
                .withEmail("user_a@gmail.com");
        userRepo.save(user1);

        Optional<User> user2 = userRepo.userOfEmail("user_a@gmail.com");
        assertTrue(user2.isPresent());
        assertEquals(user1.email(), user2.get().email());
        assertEquals(user1.password(), user2.get().password());
    }
}
