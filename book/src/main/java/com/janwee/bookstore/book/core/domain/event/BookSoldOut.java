package com.janwee.bookstore.book.core.domain.event;

import com.janwee.bookstore.foundation.event.DomainEvent;
import com.janwee.bookstore.foundation.event.Event;

public class BookSoldOut extends DomainEvent implements Event {
    private final long orderId;

    private final long bookId;

    public BookSoldOut(long orderId, long bookId) {
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
