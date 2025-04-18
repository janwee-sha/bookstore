package com.janwee.bookstore.book.presentation.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.model.Price;
import com.janwee.bookstore.foundation.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Saving book command",
        requiredProperties = {"id", "name", "price", "amount", "publishedAt", "publisher", "authorId"})
public class UpdatingBookRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 6824730576154119263L;

    @Schema(description = "ID")
    @NotNull(groups = ValidationGroup.Modification.class, message = "id required")
    private Long id;

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

    @Schema(description = "publication date")
    @NotNull(message = "publishedAt required")
    private LocalDate publishedAt;

    @Schema(description = "publisher")
    @NotBlank(message = "publisher required")
    private String publisher;

    @Schema(description = "author's ID")
    @NotNull(message = "authorId required")
    private Long authorId;

    public UpdatingBookRequest() {
    }

    @JsonIgnore
    public Book toBook() {
        return Book.update(this.id)
                .changeNameTo(this.name)
                .changeAmountTo(this.amount)
                .changePriceTo(this.price)
                .atPublicationDate(this.publishedAt)
                .byPublisher(this.publisher)
                .byAuthor(this.authorId);
    }
}
