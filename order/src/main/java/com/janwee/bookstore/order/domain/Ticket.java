package com.janwee.bookstore.order.domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(generator = "tickets_id_seq")
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "create_by", nullable = false)
    private LocalDateTime createdBy;

    public Ticket() {
        this.createdBy = LocalDateTime.now();
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
