package com.janwee.bookstore.order.domain;

import com.janwee.bookstore.common.domain.event.DomainEvent;

import java.time.LocalDateTime;

public class OrderCreated implements DomainEvent {
    private static final long serialVersionUID = 5299991395940582065L;
    private final String orderId;
    private final String bookId;
    private final LocalDateTime createBy;

    public OrderCreated(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("order required");
        }
        this.orderId = order.getId();
        this.bookId = order.getBookId();
        this.createBy = order.getCreateBy();
    }

    public String getOrderId() {
        return orderId;
    }

    public LocalDateTime getCreateBy() {
        return createBy;
    }

    public String getBookId() {
        return bookId;
    }
}
