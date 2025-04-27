package com.janwee.bookstore.book.presentation.message;

import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.model.Price;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Updating book request")
public class UpdatingBookRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 6824730576154119263L;

    @Schema(description = "name")
    private String name;

    @Schema(description = "price")
    private Price price;

    @Schema(description = "amount")
    @PositiveOrZero(message = "amount must not be less than zero")
    private Integer amount;

    @Schema(description = "publication date")
    private LocalDate publishedAt;

    @Schema(description = "publisher")
    private String publisher;

    @Schema(description = "author's ID")
    private Long authorId;

    public UpdatingBookRequest() {
    }

    public Book changedInfoOf(Book existingBook) {
        if (this.getName() != null) {
            existingBook.changeNameTo(this.getName());
        }
        if (this.getAmount() != null) {
            existingBook.changeAmountTo(this.getAmount());
        }
        if (this.getPrice() != null) {
            existingBook.changePriceTo(this.getPrice());
        }
        if (this.getPublishedAt() != null) {
            existingBook.changePublicationDate(this.getPublishedAt());
        }
        if (this.getPublisher() != null) {
            existingBook.changePublisherTo(this.getPublisher());
        }
        if (this.getAuthorId() != null) {
            existingBook.changeAuthorTo(this.getAuthorId());
        }
        return existingBook;
    }
}
