package com.janwee.bookstore.order.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class Book implements Serializable {
    private String id;

    private String name;

    private int amount;

    private BigDecimal price;

    private LocalDate publishBy;

    private String publisher;

    private Long authorId;

    public Book() {
    }
}
