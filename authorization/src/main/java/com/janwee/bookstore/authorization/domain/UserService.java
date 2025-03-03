package com.janwee.bookstore.authorization.domain;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User userOfUsername(String username);
}