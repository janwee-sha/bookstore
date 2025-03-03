package com.janwee.bookstore.authorization.infrastructure.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.janwee.bookstore.authorization.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
public class SecurityBasedUser implements User {
    private long id;

    private String email;

    @JsonIgnore
    private String password;

    private static final List<GrantedAuthority> ROLE_USER = Collections
            .unmodifiableList(AuthorityUtils.createAuthorityList("ROLE_USER"));

    public SecurityBasedUser() {
    }

    public SecurityBasedUser(long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    @Override
    public long id() {
        return id;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String password() {
        return password;
    }

    @Override
    public User ofId(long id) {
        this.id = id;
        return this;
    }

    @Override
    public User withEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public User identifiedBy(String password) {
        this.password = password;
        return this;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return ROLE_USER;
    }

    @Override
    public String getUsername() {
        return email();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}