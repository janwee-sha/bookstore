package com.janwee.bookstore.order.core.northbound.message;


import com.janwee.bookstore.foundation.event.DomainEvent;
import com.janwee.bookstore.foundation.event.Event;

import java.io.Serial;

public class BookOrdered extends DomainEvent implements Event {
    @Serial
    private static final long serialVersionUID = -215640306818366223L;

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
