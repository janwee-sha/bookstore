package com.janwee.bookstore.book.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tbl_book")
//@Cacheable
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Book implements Serializable {
    private static final long serialVersionUID = 2266684333872541496L;
    @Id
    @GeneratedValue(generator = "tbl_book_id_seq")
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

    public void sell(int amount) {
        if (this.amount < amount) {
            throw new BookSoldOutException();
        }
        this.amount -= amount;
    }
}
