package com.janwee.bookstore.authorization.southbound.adapter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.janwee.bookstore.authorization.domain.Role;
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
public class SpringSecurityUser implements UserDetails {
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

    SpringSecurityUser(long id, String email, String password, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.authorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.value()))
                .toList();
    }

    @Override
    public String getUsername() {
        return email;
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
