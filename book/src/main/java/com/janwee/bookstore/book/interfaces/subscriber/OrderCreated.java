package com.janwee.bookstore.book.interfaces.subscriber;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.janwee.bookstore.foundation.event.DefaultEvent;
import com.janwee.bookstore.foundation.event.Event;

import java.io.Serial;

public class OrderCreated extends DefaultEvent implements Event {
    @Serial
    private static final long serialVersionUID = -8988673746522591894L;

    @JsonProperty
    private final Long orderId;
    @JsonProperty
    private final Long bookId;
    @JsonProperty
    private final int amount;

    @JsonCreator
    public OrderCreated(@JsonProperty("orderId") Long orderId,
                        @JsonProperty("bookId") Long bookId,
                        @JsonProperty("amount") int amount) {
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
