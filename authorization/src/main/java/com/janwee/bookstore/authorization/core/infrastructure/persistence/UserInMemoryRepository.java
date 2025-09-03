package com.janwee.bookstore.authorization.core.infrastructure.persistence;

import com.janwee.bookstore.authorization.core.domain.Role;
import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.domain.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserInMemoryRepository implements UserRepository {
    private final Map<String, User> usersByEmail;

    private final AtomicLong SEQ_ID = new AtomicLong(1);

    public UserInMemoryRepository(@Value("${spring.security.user.name}") String adminEmail,
                                  @Value("${spring.security.user.password}") String adminPassword) {
        this.usersByEmail = new HashMap<>() {{
            put(adminEmail,
                    new SecurityBasedUser().ofId(1).withEmail(adminEmail)
                            .ofRole(Role.ADMIN)
                            .identifiedBy(adminPassword));
        }};
    }

    @Override
    public Optional<User> userOfEmail(String email) {
        return Optional.ofNullable(usersByEmail.get(email));
    }

    @Override
    public List<User> users() {
        return new ArrayList<>(usersByEmail.values());
    }

    @Override
    public void save(User user) {
        if (!usersByEmail.containsKey(user.email())) {
            user = user.ofId(SEQ_ID.addAndGet(1));
        }
        usersByEmail.put(user.email(), user);
    }
}
