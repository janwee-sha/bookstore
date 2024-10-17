package com.janwee.bookstore.order.southbound.message;

import com.janwee.bookstore.foundation.event.DomainEvent;
import com.janwee.bookstore.foundation.event.Event;

import java.time.LocalDateTime;

public class OrderCreated extends DomainEvent implements Event {
    private final Long orderId;
    private final Long bookId;
    private final int amount;
    private final LocalDateTime createdBy;

    public OrderCreated(Long orderId, Long bookId, int amount, LocalDateTime createdBy) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.amount = amount;
        this.createdBy = createdBy;
    }

    public Long orderId() {
        return orderId;
    }

    public Long bookId() {
        return bookId;
    }

    public int amount() {
        return amount;
    }

    public LocalDateTime createdBy() {
        return createdBy;
    }

    @Override
    public String toString() {
        return "OrderCreated{" +
                "orderId='" + orderId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", amount=" + amount +
                ", createdBy=" + createdBy +
                '}';
    }
}
