package com.janwee.bookstore.book.domain.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.janwee.bookstore.foundation.event.DefaultEvent;
import com.janwee.bookstore.foundation.event.Event;

import java.io.Serial;

public class BookSoldOut extends DefaultEvent implements Event {
    @Serial
    private static final long serialVersionUID = 3000442259093697821L;

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
