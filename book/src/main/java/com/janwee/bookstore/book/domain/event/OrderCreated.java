package com.janwee.bookstore.book.domain.event;

import com.janwee.bookstore.foundation.event.DomainEvent;
import com.janwee.bookstore.foundation.event.Event;

public class OrderCreated extends DomainEvent implements Event {
    private final Long orderId;
    private final Long bookId;
    private final int amount;

    public OrderCreated(Long orderId, Long bookId, int amount) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.amount = amount;
    }

    @Override
    public String description() {
        return "ORDER_CREATED";
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

    @Override
    public String toString() {
        return "OrderCreated{" +
                "orderId='" + orderId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", amount=" + amount +
                '}';
    }
}
