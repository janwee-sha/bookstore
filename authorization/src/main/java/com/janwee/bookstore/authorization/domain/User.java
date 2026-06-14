package com.janwee.bookstore.authorization.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 7981489020709759951L;
    private long id;
    private String email;
    @JsonIgnore
    private String password;
    private Role role;

    public User() {
        this.role = Role.USER;
    }

    public User(long id, String email, String password, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public long id() {
        return id;
    }

    public String email() {
        return email;
    }

    public String username() {
        return email;
    }

    public String password() {
        return password;
    }

    public Role role() {
        return role;
    }

    public List<Authority> authorities() {
        return role.authorities();
    }

    public void changePasswordTo(String newPassword) {
        this.password = newPassword;
    }

    public static class Builder {
        private final User user;

        public Builder() {
            user = new User();
        }

        public Builder id(long id) {
            user.id = id;
            return this;
        }

        public Builder email(String email) {
            user.email = email;
            return this;
        }

        public Builder password(String password) {
            user.password = password;
            return this;
        }

        public Builder role(Role role) {
            user.role = role;
            return this;
        }

        public User build() {
            return user;
        }
    }
}
