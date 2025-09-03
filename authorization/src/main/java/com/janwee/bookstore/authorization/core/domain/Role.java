package com.janwee.bookstore.authorization.core.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collections;
import java.util.List;

public enum Role {
    ADMIN(Collections
            .unmodifiableList(AuthorityUtils.createAuthorityList(Authority.USER_READ.value(),
                    Authority.USER_WRITE.value()))),
    USER(Collections
            .unmodifiableList(AuthorityUtils.createAuthorityList(Authority.USER_READ.value())));
    private final List<GrantedAuthority> authorities;

    public List<GrantedAuthority> authorities() {
        return authorities;
    }

    Role(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
