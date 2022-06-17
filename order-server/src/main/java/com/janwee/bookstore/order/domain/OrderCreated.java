package com.janwee.bookstore.order.domain;

import com.janwee.bookstore.common.domain.event.DomainEvent;

import java.time.LocalDateTime;

public class OrderCreated implements DomainEvent {
    private final String orderId;
    private final LocalDateTime createBy;

    public OrderCreated(String orderId, LocalDateTime createBy) {
        this.orderId = orderId;
        this.createBy = createBy;
    }

    public String getOrderId() {
        return orderId;
    }

    public LocalDateTime getCreateBy() {
        return createBy;
    }
}
