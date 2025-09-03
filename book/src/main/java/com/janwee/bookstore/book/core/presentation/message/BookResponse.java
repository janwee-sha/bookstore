package com.janwee.bookstore.book.core.presentation.message;

import com.janwee.bookstore.book.core.domain.model.Author;
import com.janwee.bookstore.book.core.domain.model.Book;
import com.janwee.bookstore.book.core.domain.model.Price;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Schema(title = "Book")
@Getter
@Setter
public class BookResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 4874660886108568257L;
    @Schema(title = "ID")
    private Long id;

    @Schema(title = "Name")
    private String name;

    @Schema(title = "Price")
    private Price price;

    @Schema(title = "Amount")
    private int amount;

    @Schema(title = "Publication Date")
    private LocalDate publishedAt;

    @Schema(name = "Publisher")
    private String publisher;

    @Schema(name = "Author")
    private Author author;

    public BookResponse() {
    }

    public static BookResponse from(Book book) {
        if (book != null) {
            BookResponse response = new BookResponse();
            response.id = book.id();
            response.name = book.name();
            response.price = book.price();
            response.amount = book.amount();
            response.publisher = book.publisher();
            response.publishedAt = book.publishedAt();
            return response;
        }
        return null;
    }
}
