package com.janwee.bookstore.order.southbound.message;

import com.janwee.bookstore.foundation.event.DomainEvent;
import com.janwee.bookstore.foundation.event.Event;

import java.time.LocalDateTime;

public class OrderCreated extends DomainEvent implements Event {
    private final Long orderId;
    private final Long bookId;
    private final int amount;
    private final LocalDateTime createBy;

    public OrderCreated(Long orderId, Long bookId, int amount, LocalDateTime createBy) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.amount = amount;
        this.createBy = createBy;
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

    public LocalDateTime createBy() {
        return createBy;
    }

    @Override
    public String toString() {
        return "OrderCreated{" +
                "orderId='" + orderId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", amount=" + amount +
                ", createBy=" + createBy +
                '}';
    }
}
