package com.janwee.bookstore.bookserver.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tbl_book")
public class Book implements Serializable {
    private static final long serialVersionUID = 2266684333872541496L;
    @Id
    private String id;

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
}
