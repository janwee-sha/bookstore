package com.janwee.bookstore.foundation.exception;

/**
 * TODO add comments here
 *
 * @author Janwee Xia
 * @version 1.0
 * @since 2024/3/8
 */
public abstract class BadRequestException extends DomainException {
    private static final long serialVersionUID = 374294020417040197L;

    public BadRequestException(String message) {
        super(message);
    }
}
