package com.janwee.bookstore.foundation.exception;

import java.io.Serial;

public abstract class DomainException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1499926993701124659L;

    public DomainException(String message) {
        super(message);
    }
}
