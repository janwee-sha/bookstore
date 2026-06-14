package com.janwee.bookstore.authorization.domain;

import java.util.List;

public enum Role {
    ADMIN(List.of(Authority.USER_READ, Authority.USER_WRITE)),
    USER(List.of(Authority.USER_READ));

    private final List<Authority> authorities;

    public List<Authority> authorities() {
        return authorities;
    }

    Role(List<Authority> authorities) {
        this.authorities = authorities;
    }
}
