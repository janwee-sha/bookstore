package com.janwee.bookstore.authorization.presentation.message;

import com.janwee.bookstore.authorization.domain.User;
import com.janwee.bookstore.authorization.infrastructure.security.SecurityBasedUser;
import lombok.Getter;

@Getter
public class RegisteringUserRequest {

    private String email;

    private String password;

    public User toUser() {
        return new SecurityBasedUser().withEmail(email).identifiedBy(password);
    }
}
