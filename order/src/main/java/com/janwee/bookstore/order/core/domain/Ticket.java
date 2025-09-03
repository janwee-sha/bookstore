package com.janwee.bookstore.order.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tickets")
public class Ticket implements Serializable {
    @Serial
    private static final long serialVersionUID = -2738047778557352396L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long orderId;

    private Long bookId;

    private LocalDateTime createdAt;

    public Ticket() {
        this.createdAt = LocalDateTime.now();
    }

    public Ticket ofOrder(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public Ticket ofBook(Long bookId) {
        this.bookId = bookId;
        return this;
    }
}
