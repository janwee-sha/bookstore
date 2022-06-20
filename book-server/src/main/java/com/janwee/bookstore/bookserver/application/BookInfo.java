package com.janwee.bookstore.bookserver.application;

import com.janwee.bookstore.bookserver.domain.Author;
import com.janwee.bookstore.bookserver.domain.Book;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
public class BookInfo {
    private Long id;
    private String name;
    private BigDecimal price;
    private LocalDate publishBy;
    private String publisher;
    private Author author;

    public BookInfo() {
    }

    public BookInfo(Book book, Author author) {
        if (book != null) {
            BeanUtils.copyProperties(book, this);
        }
        if (author != null) {
            this.author = author;
        }
    }
}
