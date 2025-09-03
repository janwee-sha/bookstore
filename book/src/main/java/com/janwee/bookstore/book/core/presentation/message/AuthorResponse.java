package com.janwee.bookstore.book.core.presentation.message;

import com.janwee.bookstore.book.core.domain.model.Author;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(title = "Author")
public class AuthorResponse implements Serializable {
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

    public AuthorResponse() {
    }

    public static AuthorResponse from(Author author) {
        if (author == null) {
            return null;
        }
        AuthorResponse response = new AuthorResponse();
        BeanUtils.copyProperties(author, response);
        return response;
    }
}
