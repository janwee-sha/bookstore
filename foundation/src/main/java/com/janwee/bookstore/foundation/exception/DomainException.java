package com.janwee.bookstore.foundation.exception;

/**
 * TODO add comments here
 *
 * @author Janwee Xia
 * @version 1.0
 * @since 2024/3/9
 */
public abstract class DomainException extends RuntimeException {
    private static final long serialVersionUID = -1499926993701124659L;

    public DomainException(String message) {
        super(message);
    }
}
