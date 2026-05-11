package com.janwee.bookstore.authorization.core.infrastructure.persistence;

import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.domain.UserRepository;
import com.janwee.bookstore.authorization.core.infrastructure.persistence.jpa.SecurityBasedUserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SecurityBasedUserRepositoryImpl implements UserRepository {
    private final SecurityBasedUserJpaRepository userJpaRepo;

    @Override
    public Optional<User> userOfEmail(String email) {
        return Optional.ofNullable(userJpaRepo.findByEmail(email).orElse(null));
    }

    @Override
    public List<User> users() {
        return new ArrayList<>(userJpaRepo.findAll());
    }

    public User save(User user) {
        if (user instanceof SecurityBasedUser securityBasedUser) {
            return userJpaRepo.save(securityBasedUser);
        }
        throw new IllegalArgumentException("Unsupported user type: " + user.getClass().getName());
    }
}
