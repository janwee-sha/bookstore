package com.janwee.bookstore.common.domain.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRejected implements DomainEvent {
    private static final long serialVersionUID = -7372746002589252721L;
    private String orderId;

    public OrderRejected() {
    }

    public OrderRejected(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "OrderRejected{" +
                "orderId='" + orderId + '\'' +
                '}';
    }
}
