package com.janwee.bookstore.authorization.infrastructure.persistence;

import com.janwee.bookstore.authorization.domain.User;
import com.janwee.bookstore.authorization.domain.UserRepository;
import com.janwee.bookstore.authorization.infrastructure.security.SecurityBasedUser;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class UserInMemoryRepository implements UserRepository {
    private final Map<String, User> usersByEmail;

    public UserInMemoryRepository() {
        String encryptedPass = "{bcrypt}$2a$10$h/AJueu7Xt9yh3qYuAXtk.WZJ544Uc2kdOKlHu2qQzCh/A3rq46qm";
        this.usersByEmail = Map.of("bookstore-admin-0@outlook.com",
                new SecurityBasedUser().ofId(1).withEmail("bookstore-admin-0@outlook.com")
                        .identifiedBy(encryptedPass));
    }

    @Override
    public Optional<User> userOfEmail(String email) {
        return Optional.ofNullable(usersByEmail.get(email));
    }
}
