package com.janwee.bookstore.authorization.northbound.message;

import com.janwee.bookstore.authorization.domain.Role;
import com.janwee.bookstore.authorization.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Schema(title = "Registering User Request", description = "Request to register a new user")
public class SigningUpRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 7959543857288854933L;

    @NotBlank(message = "Email required")
    @Email(message = "Invalid email format")
    @Schema(title = "Email", description = "Email address of the user", example = "abc@email.com")
    private String email;

    @NotBlank(message = "Password required")
    @Schema(title = "Password", description = "Password of the user", example = "P@ssw0rd!")
    private String password;

    public User toUser() {
        return new User.Builder().email(email)
                .role(Role.USER)
                .password(password)
                .build();
    }
}
