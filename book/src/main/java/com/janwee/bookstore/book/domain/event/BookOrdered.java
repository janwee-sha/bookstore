package com.janwee.bookstore.book.domain.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.janwee.bookstore.foundation.event.DomainEvent;
import com.janwee.bookstore.foundation.event.Event;

import java.io.Serial;

public class BookOrdered extends DomainEvent implements Event {
    @Serial
    private static final long serialVersionUID = -8710269418141302043L;

    @JsonProperty
    private final long orderId;

    @JsonProperty
    private final long bookId;

    @JsonCreator
    public BookOrdered(@JsonProperty("orderId") long orderId,
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
