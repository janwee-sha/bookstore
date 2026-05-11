package com.janwee.bookstore.authorization.core.domain;

public interface User {
    long id();

    String email();

    String username();

    String password();

    void changePasswordTo(String newPassword);
}
