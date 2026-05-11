package com.janwee.bookstore.authorization.core.southbound.adapter;

import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.southbound.port.UserRepository;
import com.janwee.bookstore.authorization.core.southbound.adapter.jpa.SpringSecurityUserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpringSecurityUserRepositoryJpaAdapter implements UserRepository {
    private final SpringSecurityUserJpaRepository userJpaRepo;

    @Override
    public Optional<User> userOfEmail(String email) {
        return Optional.ofNullable(userJpaRepo.findByEmail(email).orElse(null));
    }

    @Override
    public List<User> users() {
        return new ArrayList<>(userJpaRepo.findAll());
    }

    public User save(User user) {
        if (user instanceof SpringSecurityUser securityBasedUser) {
            return userJpaRepo.save(securityBasedUser);
        }
        throw new IllegalArgumentException("Unsupported user type: " + user.getClass().getName());
    }
}
