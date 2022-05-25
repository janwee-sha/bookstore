package com.janwee.bookstore.bookserver.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tbl_book")
public class Book implements Serializable {
    @Id
    @GeneratedValue(generator = "tbl_book_id_seq")
    private Long id;

    @NotBlank(message = "name required")
    @Column(name = "book_name", nullable = false)
    private String name;

//    @NotNull(message = "currencyOfPrice required")
//    @Column(name = "price_currency", nullable = false)
//    private Currency currencyOfPrice;

    @NotNull(message = "price required")
    @DecimalMin(value = "0.0", message = "price must not be less than zero")
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @NotNull(message = "publish_by required")
    @Column(name = "publish_by", nullable = false)
    private LocalDate publishBy;

    @NotBlank
    @Column(name = "publisher", nullable = false)
    private String publisher;

    @NotNull
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    public Book() {
    }
}
