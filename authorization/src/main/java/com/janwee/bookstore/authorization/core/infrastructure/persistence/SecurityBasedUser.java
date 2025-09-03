package com.janwee.bookstore.authorization.core.infrastructure.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.janwee.bookstore.authorization.core.domain.Role;
import com.janwee.bookstore.authorization.core.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

@Getter
public class SecurityBasedUser implements User {
    @Serial
    private static final long serialVersionUID = 7990143688548707092L;

    private long id;

    private String email;

    @JsonIgnore
    private String password;

    private Role role;

    public SecurityBasedUser() {
        this.role = Role.USER;
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
    public String username() {
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
        return role.authorities();
    }

    @Override
    public User ofRole(Role role) {
        this.role = role;
        return this;
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