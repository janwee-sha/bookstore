package com.janwee.bookstore.book.application.view;

import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.model.Currency;
import com.janwee.bookstore.book.domain.model.Price;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(title = "Book")
@Getter
@Setter
public class BookView implements Serializable {
    @Serial
    private static final long serialVersionUID = 4874660886108568257L;
    @Schema(title = "ID")
    private Long id;

    @Schema(title = "Name")
    private String name;

    @Schema(title = "Price")
    private PriceView price;

    @Schema(title = "Amount")
    private int amount;

    @Schema(title = "Publication Date")
    private LocalDate publishedAt;

    @Schema(name = "Publisher")
    private String publisher;

    @Schema(name = "Author")
    private AuthorView author;

    public BookView() {
    }

    public static BookView from(Book book) {
        if (book != null) {
            BookView response = new BookView();
            response.id = book.id();
            response.name = book.name();
            response.price = PriceView.fromPrice(book.price());
            response.amount = book.amount();
            response.publisher = book.publisher();
            response.publishedAt = book.publishedAt();
            return response;
        }
        return null;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceView implements Serializable {

        @Serial
        private static final long serialVersionUID = 7366372589098101989L;

        @Schema(title = "Currency")
        private Currency currency;

        @Schema(title = "Amount")
        private BigDecimal amount;

        public static PriceView fromPrice(Price price) {
            return price == null ? null : new PriceView(price.currency(), price.amount());
        }
    }
}
