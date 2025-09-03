package com.janwee.bookstore.foundation.exception;

import java.io.Serial;

public abstract class BadRequestException extends DomainException {
    @Serial
    private static final long serialVersionUID = 374294020417040197L;

    public BadRequestException(String message) {
        super(message);
    }
}
