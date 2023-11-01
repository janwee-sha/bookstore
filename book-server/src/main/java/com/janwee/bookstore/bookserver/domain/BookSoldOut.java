package com.janwee.bookstore.bookserver.domain;

public class BookSoldOut implements Event {
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

    @Override
    public String description() {
        return "BOOK_SOLD_OUT";
    }
}
