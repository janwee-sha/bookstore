package com.janwee.bookstore.book.presentation.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.model.Price;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(title = "Publishing Book Request",
        requiredProperties = {"name", "price", "amount", "publishedAt", "publisher", "authorId"})
public class PublishingBookRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 6824730576154119263L;

    @Schema(title = "Name")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(title = "Price")
    @NotNull(message = "Price is required")
    private Price price;

    @Schema(title = "Amount")
    @NotNull(message = "Amount is required")
    @PositiveOrZero(message = "Amount should not be negative")
    private int amount;

    @Schema(title = "Publisher")
    @NotBlank(message = "Publisher is required")
    private String publisher;

    @Schema(title = "Author ID")
    @NotNull(message = "Author ID is required")
    private Long authorId;

    public PublishingBookRequest() {
    }

    @JsonIgnore
    public Book toNewBook() {
        return new Book.Builder()
                .withName(this.name)
                .withAmount(this.amount)
                .withPrice(this.price)
                .byPublisher(this.publisher)
                .byAuthor(this.authorId)
                .build();
    }
}
