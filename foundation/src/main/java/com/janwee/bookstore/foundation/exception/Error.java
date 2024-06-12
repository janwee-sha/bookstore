package com.janwee.bookstore.foundation.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Error {
    private LocalDateTime timestamp;
    private final String message;
    private String path;

    public Error(String message) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
    }

    public static Error ofMessage(String message) {
        return new Error(message);
    }

    public Error ofPath(String path) {
        this.path = path;
        return this;
    }
}
