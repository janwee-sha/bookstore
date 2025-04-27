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
@Schema(description = "Saving book command",
        requiredProperties = {"name", "price", "amount", "publishedAt", "publisher", "authorId"})
public class PublishingBookRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 6824730576154119263L;

    @Schema(description = "name")
    @NotBlank(message = "name required")
    private String name;

    @Schema(description = "price")
    @NotNull(message = "price required")
    private Price price;

    @Schema(description = "amount")
    @NotNull(message = "amount required")
    @PositiveOrZero(message = "amount must not be less than zero")
    private int amount;

    @Schema(description = "publisher")
    @NotBlank(message = "publisher required")
    private String publisher;

    @Schema(description = "author's ID")
    @NotNull(message = "authorId required")
    private Long authorId;

    public PublishingBookRequest() {
    }

    @JsonIgnore
    public Book toNewBook() {
        Book book = new Book();
        book.changeNameTo(this.name);
        book.changeAmountTo(this.amount);
        book.changePriceTo(this.price);
        book.changePublisherTo(this.publisher);
        book.changeAuthorTo(this.authorId);
        return book;
    }
}
