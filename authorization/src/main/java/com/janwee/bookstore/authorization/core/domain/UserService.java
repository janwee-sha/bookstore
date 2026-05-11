package com.janwee.bookstore.authorization.core.domain;

public interface UserService {
    User userOfUsername(String username);

    void signup(User user);
}