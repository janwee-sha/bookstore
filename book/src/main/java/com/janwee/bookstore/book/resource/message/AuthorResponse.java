package com.janwee.bookstore.book.resource.message;

import com.janwee.bookstore.book.domain.model.Author;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Getter
@Setter
@Schema(description = "Author Response")
public class AuthorResponse implements Serializable {
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "profile")
    private String profile;

    @Schema(description = "profile")
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
