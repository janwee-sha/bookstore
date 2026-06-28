package com.janwee.bookstore.order.domain;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class Ticket implements Serializable {
    @Serial
    private static final long serialVersionUID = -2738047778557352396L;

    private Long id;

    private Long orderId;

    private Long bookId;

    private LocalDateTime createdAt;

    public Ticket() {
        this.createdAt = LocalDateTime.now();
    }

    public Ticket(Long id, Long orderId, Long bookId, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.bookId = bookId;
        this.createdAt = createdAt;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public Long id() {
        return id;
    }

    public Long orderId() {
        return orderId;
    }

    public Long bookId() {
        return bookId;
    }

    public LocalDateTime createdAt() {
        return createdAt;
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
