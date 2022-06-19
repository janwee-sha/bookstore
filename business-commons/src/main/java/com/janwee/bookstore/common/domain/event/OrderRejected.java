package com.janwee.bookstore.common.domain.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRejected implements DomainEvent {
    private static final long serialVersionUID = 153249684583902383L;
    private String orderId;

    public OrderRejected(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "OrderCreated{" +
                "orderId='" + orderId + '\'' +
                '}';
    }
}
