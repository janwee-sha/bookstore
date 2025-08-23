package integration.com.janwee.bookstore.authorization;

import com.janwee.bookstore.authorization.domain.Role;
import com.janwee.bookstore.authorization.domain.User;
import com.janwee.bookstore.authorization.infrastructure.persistence.UserInMemoryRepository;
import com.janwee.bookstore.authorization.infrastructure.security.SecurityBasedUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryIntegrationTest {
    @InjectMocks
    private UserInMemoryRepository userRepo;

    @Test
    void testSavingUser() {
        User user1 = new SecurityBasedUser()
                .identifiedBy("pass")
                .ofRole(Role.USER)
                .withEmail("user_a@gmail.com");
        userRepo.save(user1);

        Optional<User> user2 = userRepo.userOfEmail("user_a@gmail.com");
        assertTrue(user2.isPresent());
        assertEquals(user1.email(), user2.get().email());
        assertEquals(user1.password(), user2.get().password());
    }
}
