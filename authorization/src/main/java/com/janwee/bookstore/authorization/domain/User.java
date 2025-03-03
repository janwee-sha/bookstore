package com.janwee.bookstore.authorization.domain;

import org.springframework.security.core.userdetails.UserDetails;

public interface User extends UserDetails {
    long id();

    String email();

    String username();

    String password();

    User ofId(long id);

    User withEmail(String email);

    User identifiedBy(String password);
}
