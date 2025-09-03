package com.janwee.bookstore.book.core.domain.event;

import com.janwee.bookstore.foundation.event.DomainEvent;
import com.janwee.bookstore.foundation.event.Event;

import java.io.Serial;

public class BookOrdered extends DomainEvent implements Event {
    @Serial
    private static final long serialVersionUID = -8710269418141302043L;

    private final long orderId;

    private final long bookId;

    public BookOrdered(long orderId, long bookId) {
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
