package com.janwee.bookstore.common.domain.event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderCreated implements DomainEvent {
    private static final long serialVersionUID = 153249684583902383L;
    private String orderId;
    private String bookId;
    private LocalDateTime createBy;

    public OrderCreated(String orderId, String bookId, LocalDateTime createBy) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.createBy = createBy;
    }

    @Override
    public String toString() {
        return "OrderCreated{" +
                "orderId='" + orderId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", createBy=" + createBy +
                '}';
    }
}
