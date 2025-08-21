package com.janwee.bookstore.foundation.exception;

import java.io.Serial;

/**
 * TODO add comments here
 *
 * @author Janwee Xia
 * @version 1.0
 * @since 2024/3/8
 */
public abstract class BadRequestException extends DomainException {
    @Serial
    private static final long serialVersionUID = 374294020417040197L;

    public BadRequestException(String message) {
        super(message);
    }
}
