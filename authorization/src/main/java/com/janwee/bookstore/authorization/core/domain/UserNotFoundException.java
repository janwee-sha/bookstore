package com.janwee.bookstore.authorization.core.domain;

import com.janwee.bookstore.foundation.exception.NotFoundException;

import java.io.Serial;

public class UserNotFoundException extends NotFoundException {

    @Serial
    private static final long serialVersionUID = -6374099829525557702L;

    public UserNotFoundException(String username) {
        super("No such user of username: " + username);
    }
}