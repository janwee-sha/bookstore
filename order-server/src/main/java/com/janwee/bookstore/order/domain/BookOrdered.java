package com.janwee.bookstore.order.domain;

public class BookOrdered implements Event {
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

    @Override
    public String description() {
        return "BOOK_ORDERED";
    }
}
