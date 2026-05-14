package com.janwee.bookstore.authorization.core.domain;

public interface UserService {
    User userOf(String username);

    void add(User user);
}