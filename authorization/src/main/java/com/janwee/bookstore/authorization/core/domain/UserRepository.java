package com.janwee.bookstore.authorization.core.domain;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> userOfEmail(String email);

    List<User> users();

    void save(User user);
}
