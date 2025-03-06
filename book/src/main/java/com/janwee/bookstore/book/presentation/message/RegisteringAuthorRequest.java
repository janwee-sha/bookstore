package com.janwee.bookstore.book.presentation.message;

import com.janwee.bookstore.book.domain.model.Author;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Getter
@Setter
@Schema(description = "Registering Author Request", requiredProperties = {"name"})
public class RegisteringAuthorRequest implements Serializable {

    @Schema(description = "name")
    @NotBlank(message = "name required")
    private String name;

    @Schema(description = "profile")
    private String profile;

    @Schema(description = "profile")
    private String phoneNumber;

    public RegisteringAuthorRequest() {
    }

    public Author toAuthor() {
        Author author = new Author();
        BeanUtils.copyProperties(this, author);
        return author;
    }
}
