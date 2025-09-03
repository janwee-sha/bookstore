package com.janwee.bookstore.authorization.core.presentation.message;

import com.janwee.bookstore.authorization.core.domain.Role;
import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.infrastructure.persistence.SecurityBasedUser;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class RegisteringUserRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 7959543857288854933L;

    private String email;

    private String password;

    public User toUser() {
        return new SecurityBasedUser().withEmail(email)
                .ofRole(Role.USER)
                .identifiedBy(password);
    }
}