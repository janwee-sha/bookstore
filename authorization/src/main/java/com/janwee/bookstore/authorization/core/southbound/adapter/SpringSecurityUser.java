package com.janwee.bookstore.authorization.core.southbound.adapter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.janwee.bookstore.authorization.core.domain.Role;
import com.janwee.bookstore.authorization.core.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;

@Getter
@Entity
@Table(name = "user_accounts", uniqueConstraints = @UniqueConstraint(name = "uk_users_email", columnNames = "email"))
public class SpringSecurityUser implements User, UserDetails {
    @Serial
    private static final long serialVersionUID = 7990143688548707092L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public SpringSecurityUser() {
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
    public void changePasswordTo(String newPassword) {
        this.password = newPassword;
    }

    public SpringSecurityUser ofId(long id) {
        this.id = id;
        return this;
    }

    public SpringSecurityUser withEmail(String email) {
        this.email = email;
        return this;
    }

    public SpringSecurityUser identifiedBy(String password) {
        this.password = password;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.authorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.value()))
                .toList();
    }

    public SpringSecurityUser ofRole(Role role) {
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
