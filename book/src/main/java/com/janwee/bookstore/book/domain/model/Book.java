package com.janwee.bookstore.book.domain.model;

import com.janwee.bookstore.book.domain.exception.InvalidBookException;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = 2266684333872541496L;

    private Long id;

    private String name;

    private int amount;

    private Price price;

    private LocalDate publishedAt;

    private String publisher;

    private Long authorId;

    public Book() {
        this.publishedAt = LocalDate.now();
    }

    public Book(Long id, String name, int amount, Price price,
                LocalDate publishedAt, String publisher, Long authorId) {
        this();
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.publishedAt = publishedAt;
        this.publisher = publisher;
        this.authorId = authorId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long id() {
        return id;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void renameTo(String name) {
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
            throw InvalidBookException.negativePrice();
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

    public static class Builder {
        private final Book book;

        public Builder() {
            this.book = new Book();
        }

        public Builder id(Long id) {
            book.assignId(id);
            return this;
        }

        public Builder name(String name) {
            book.renameTo(name);
            return this;
        }

        public Builder amount(int amount) {
            book.changeAmountTo(amount);
            return this;
        }

        public Builder price(Price price) {
            book.changePriceTo(price);
            return this;
        }

        public Builder publishedAt(LocalDate publishedAt) {
            book.changePublicationDate(publishedAt);
            return this;
        }

        public Builder publisher(String publisher) {
            book.changePublisherTo(publisher);
            return this;
        }

        public Builder authorId(Long authorId) {
            book.changeAuthorTo(authorId);
            return this;
        }

        public Book build() {
            return book;
        }
    }
}
