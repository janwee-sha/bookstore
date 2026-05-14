package com.janwee.bookstore.authorization.core.southbound.adapter;

import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.southbound.adapter.jpa.SpringSecurityUserJpaRepository;
import com.janwee.bookstore.authorization.core.southbound.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpringSecurityUserRepositoryJpaAdapter implements UserRepository {
    private final SpringSecurityUserJpaRepository userJpaRepo;

    @Override
    public Optional<User> userOfEmail(String email) {
        return userJpaRepo.findByEmail(email)
                .map(SpringSecurityUserRepositoryJpaAdapter::toDomainUser);
    }

    @Override
    public List<User> users() {
        return userJpaRepo.findAll().stream()
                .map(SpringSecurityUserRepositoryJpaAdapter::toDomainUser)
                .toList();
    }

    public User save(User user) {
        return toDomainUser(userJpaRepo.save(toSpringSecurityUser(user)));
    }

    static User toDomainUser(SpringSecurityUser user) {
        return new User(user.getId(), user.getEmail(), user.getPassword(), user.getRole());
    }

    static SpringSecurityUser toSpringSecurityUser(User user) {
        return new SpringSecurityUser(user.id(), user.email(), user.password(), user.role());
    }
}
