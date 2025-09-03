package com.janwee.bookstore.book.core.domain.event;

import com.janwee.bookstore.foundation.event.DomainEvent;
import com.janwee.bookstore.foundation.event.Event;

import java.io.Serial;

public class OrderCreated extends DomainEvent implements Event {
    @Serial
    private static final long serialVersionUID = -8988673746522591894L;

    private final Long orderId;
    private final Long bookId;
    private final int amount;

    public OrderCreated(Long orderId, Long bookId, int amount) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.amount = amount;
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
