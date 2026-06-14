package com.janwee.bookstore.order.northbound.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.janwee.bookstore.foundation.event.DomainEvent;
import com.janwee.bookstore.foundation.event.Event;

import java.io.Serial;

public class BookSoldOut extends DomainEvent implements Event {
    @Serial
    private static final long serialVersionUID = 6196262624575790186L;

    @JsonProperty
    private final long orderId;

    @JsonProperty
    private final long bookId;

    @JsonCreator
    public BookSoldOut(@JsonProperty("orderId") long orderId,
                       @JsonProperty("bookId") long bookId) {
        this.orderId = orderId;
        this.bookId = bookId;
    }

    public long orderId() {
        return orderId;
    }

    public long bookId() {
        return bookId;
    }
}
