package com.janwee.bookstore.common.domain.event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderCreated implements DomainEvent {
    private static final long serialVersionUID = 153249684583902383L;
    private Long orderId;
    private Long bookId;
    private int amount;
    private LocalDateTime createBy;

    public OrderCreated() {
    }

    public OrderCreated(Long orderId, Long bookId, int amount, LocalDateTime createBy) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.amount = amount;
        this.createBy = createBy;
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
