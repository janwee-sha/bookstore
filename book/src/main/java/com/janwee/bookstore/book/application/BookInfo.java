package com.janwee.bookstore.book.application;

import com.janwee.bookstore.book.domain.Author;
import com.janwee.bookstore.book.domain.Book;
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
    private int amount;
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
