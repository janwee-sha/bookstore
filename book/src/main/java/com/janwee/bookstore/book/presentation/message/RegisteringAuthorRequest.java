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
@Schema(title = "Registering Author Request", requiredProperties = {"name"})
public class RegisteringAuthorRequest implements Serializable {

    @Schema(title = "name")
    @NotBlank(message = "name required")
    private String name;

    @Schema(title = "profile")
    private String profile;

    @Schema(title = "profile")
    private String phoneNumber;

    public RegisteringAuthorRequest() {
    }

    public Author toAuthor() {
        Author author = new Author();
        BeanUtils.copyProperties(this, author);
        return author;
    }
}
