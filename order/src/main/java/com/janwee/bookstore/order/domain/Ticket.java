package com.janwee.bookstore.order.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Ticket implements Serializable {
    @Serial
    private static final long serialVersionUID = -2738047778557352396L;

    private Long id;

    private Long orderId;

    private Long bookId;

    private final LocalDateTime createdAt;

    public Ticket() {
        this.createdAt = LocalDateTime.now();
    }

    public Ticket(Long id, Long orderId, Long bookId, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.bookId = bookId;
        this.createdAt = createdAt;
    }

    public static Ticket newTicketOf(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }
        Ticket ticket = new Ticket();
        ticket.orderId = order.id();
        ticket.bookId = order.bookId();
        return ticket;
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
}
