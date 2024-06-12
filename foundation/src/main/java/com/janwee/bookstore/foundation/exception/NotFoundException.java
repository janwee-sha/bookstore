package com.janwee.bookstore.foundation.exception;

/**
 * TODO add comments here
 *
 * @author Janwee Xia
 * @version 1.0
 * @since 2024/3/8
 */
public class NotFoundException extends DomainException {
    private static final long serialVersionUID = -7247463424039127929L;

    protected NotFoundException(String message) {
        super(message);
    }
}
