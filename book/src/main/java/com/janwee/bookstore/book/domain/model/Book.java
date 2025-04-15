package com.janwee.bookstore.book.domain.model;

import com.janwee.bookstore.book.domain.exception.InvalidBookException;
import com.janwee.bookstore.book.infrastructure.persistence.PriceConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = 2266684333872541496L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private int amount;

    @Convert(converter = PriceConverter.class)
    private Price price;

    private LocalDate publishedAt;

    private String publisher;

    private Long authorId;

    public Book() {
        this.publishedAt = LocalDate.now();
    }

    public static Book create() {
        return new Book();
    }

    public static Book update(long id) {
        Book book = new Book();
        book.id = id;
        return book;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Book changeNameTo(String name) {
        this.name = name;
        return this;
    }

    public int amount() {
        return amount;
    }

    public Book changeAmountTo(int amount) {
        this.amount = amount;
        return this;
    }

    public Price price() {
        return price;
    }

    public Book changePriceTo(Price price) {
        if (price == null || price.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw InvalidBookException.validPriceRequired();
        }
        this.price = price;
        return this;
    }

    public LocalDate publishedAt() {
        return publishedAt;
    }

    public Book atPublicationDate(LocalDate publishedAt) {
        this.publishedAt = publishedAt;
        return this;
    }

    public String publisher() {
        return publisher;
    }

    public Book byPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public Long authorId() {
        return authorId;
    }

    public Book byAuthor(Long authorId) {
        this.authorId = authorId;
        return this;
    }

    public void sell(int amount) {
        if (this.amount < amount) {
            throw InvalidBookException.soldOut();
        }
        this.amount -= amount;
    }
}
