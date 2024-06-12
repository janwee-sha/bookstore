package com.janwee.bookstore.book.resource.message;

import com.janwee.bookstore.book.domain.model.Author;
import com.janwee.bookstore.book.domain.model.Book;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Book")
@Getter
@Setter
public class BookResponse implements Serializable {
    private static final long serialVersionUID = 4874660886108568257L;
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "price")
    private BigDecimal price;

    @Schema
    private int amount;

    @Schema(description = "publication date")
    private LocalDate publishBy;

    @Schema(description = "some time")
    private LocalDateTime someTime = LocalDateTime.now();

    @Schema(name = "publisher")
    private String publisher;

    @Schema(name = "author")
    private Author author;

    public BookResponse() {
    }

    public static BookResponse from(Book book) {
        if (book != null) {
            BookResponse response = new BookResponse();
            BeanUtils.copyProperties(book, response);
            return response;
        }
        return null;
    }
}
