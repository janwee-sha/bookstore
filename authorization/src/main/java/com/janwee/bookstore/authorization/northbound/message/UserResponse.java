package com.janwee.bookstore.authorization.northbound.message;

import com.janwee.bookstore.authorization.domain.Role;
import com.janwee.bookstore.authorization.domain.User;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class UserResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 7981489020709759951L;
    private long id;
    private String email;
    private Role role;

    public static UserResponse from(User user) {
        UserResponse response = new UserResponse();
        response.id = user.id();
        response.email = user.email();
        response.role = user.role();
        return response;
    }
}