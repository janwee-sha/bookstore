package com.janwee.bookstore.order.southbound.message;

/**
 * TODO Add a description for the class here
 *
 * @author Will Hsia
 * @since 2024/6/13
 */
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

    public boolean isAvailable() {
        return Status.AVAILABLE.equals(this.status);
    }

    public boolean isUnavailable() {
        return Status.UNAVAILABLE.equals(this.status);
    }

    public enum Status {
        AVAILABLE,
        UNAVAILABLE
    }
}
