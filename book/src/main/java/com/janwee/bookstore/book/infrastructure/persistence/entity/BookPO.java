package com.janwee.bookstore.book.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter
public class BookPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2266684333872541496L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_books")
    @SequenceGenerator(name = "seq_books", sequenceName = "book.seq_books", allocationSize = 1)
    private Long id;

    private String name;

    private String price;

    private LocalDate publishedAt;

    private String publisher;

    private Long authorId;

    public BookPO() {
    }

    public BookPO(Long id, String name, String price, LocalDate publishedAt, String publisher, Long authorId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.publishedAt = publishedAt;
        this.publisher = publisher;
        this.authorId = authorId;
    }
}
