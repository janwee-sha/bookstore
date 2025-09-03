package com.janwee.bookstore.order.core.southbound.message;

public class BookReview {
    private final Status status;

    private BookReview(Status status) {
        this.status = status;
    }

    public static BookReview available() {
        return new BookReview(Status.AVAILABLE);
    }

    public static BookReview unavailable() {
        return new BookReview(Status.UNAVAILABLE);
    }

    public boolean isUnavailable() {
        return Status.UNAVAILABLE.equals(this.status);
    }

    public enum Status {
        AVAILABLE,
        UNAVAILABLE
    }
}
