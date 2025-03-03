package com.janwee.bookstore.authorization.domain;

import java.util.Optional;

public interface UserRepository {
    Optional<User> userOfEmail(String email);
}
