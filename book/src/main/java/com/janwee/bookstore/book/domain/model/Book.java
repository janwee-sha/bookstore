package com.janwee.bookstore.book.domain.model;

import com.janwee.bookstore.book.domain.exception.InvalidBookException;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

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

    @Column(name = "book_name", nullable = false)
    private String name;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "publish_by", nullable = false)
    private LocalDate publishBy;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    public Book() {
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public int amount() {
        return amount;
    }

    public BigDecimal price() {
        return price;
    }

    public LocalDate publishBy() {
        return publishBy;
    }

    public String publisher() {
        return publisher;
    }

    public Long authorId() {
        return authorId;
    }

    public void sell(int amount) {
        if (this.amount < amount) {
            throw InvalidBookException.soldOut();
        }
        this.amount -= amount;
    }
}
