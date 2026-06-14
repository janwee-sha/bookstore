package com.janwee.bookstore.authorization.southbound.port;

import com.janwee.bookstore.authorization.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> userOfEmail(String email);

    List<User> users();

    User save(User user);
}
