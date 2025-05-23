package com.janwee.bookstore.authorization.domain;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserManager extends UserDetailsService {
    User userOfUsername(String username);

    void createUser(User user);
}