package com.janwee.bookstore.book.application.view;

import com.janwee.bookstore.book.domain.model.Author;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(title = "Author")
public class AuthorView implements Serializable {
    @Serial
    private static final long serialVersionUID = -5381508957577224951L;
    @Schema(title = "ID")
    private Long id;

    @Schema(title = "Name")
    private String name;

    @Schema(title = "Profile")
    private String profile;

    @Schema(title = "Phone Number")
    private String phoneNumber;

    public AuthorView() {
    }

    public static AuthorView from(Author author) {
        if (author == null) {
            return null;
        }
        AuthorView response = new AuthorView();
        response.id = author.id();
        response.name = author.name();
        response.profile = author.profile();
        response.phoneNumber = author.phoneNumber();
        return response;
    }
}
