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

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public void changeNameTo(String name) {
        this.name = name;
    }

    public int amount() {
        return amount;
    }

    public void changeAmountTo(int amount) {
        if (amount < 0) {
            throw InvalidBookException.negativeAmount();
        }
        this.amount = amount;
    }

    public Price price() {
        return price;
    }

    public void changePriceTo(Price price) {
        if (price == null || price.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw InvalidBookException.validPriceRequired();
        }
        this.price = price;
    }

    public LocalDate publishedAt() {
        return publishedAt;
    }

    public void changePublicationDate(LocalDate publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String publisher() {
        return publisher;
    }

    public void changePublisherTo(String publisher) {
        this.publisher = publisher;
    }

    public Long authorId() {
        return authorId;
    }

    public void changeAuthorTo(Long authorId) {
        this.authorId = authorId;
    }

    public void sell(int amount) {
        if (this.amount < amount) {
            throw InvalidBookException.soldOut();
        }
        this.amount -= amount;
    }
}
