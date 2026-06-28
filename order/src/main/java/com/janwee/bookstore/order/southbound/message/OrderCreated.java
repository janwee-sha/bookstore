package com.janwee.bookstore.order.southbound.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.janwee.bookstore.foundation.event.DefaultEvent;
import com.janwee.bookstore.foundation.event.Event;

import java.io.Serial;
import java.time.LocalDateTime;

public class OrderCreated extends DefaultEvent implements Event {
    @Serial
    private static final long serialVersionUID = -7501613599101584679L;

    @JsonProperty
    private final Long orderId;
    @JsonProperty
    private final Long bookId;
    @JsonProperty
    private final int amount;
    @JsonProperty
    private final LocalDateTime createdBy;

    @JsonCreator
    public OrderCreated(@JsonProperty("orderId") Long orderId,
                        @JsonProperty("bookId") Long bookId,
                        @JsonProperty("amount") int amount,
                        @JsonProperty("createdBy") LocalDateTime createdBy) {
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
