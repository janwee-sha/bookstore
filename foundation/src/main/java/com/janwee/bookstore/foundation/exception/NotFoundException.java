package com.janwee.bookstore.foundation.exception;

import java.io.Serial;

public class NotFoundException extends DomainException {
    @Serial
    private static final long serialVersionUID = -7247463424039127929L;

    protected NotFoundException(String code, String message) {
        super(code, message);
    }
}
