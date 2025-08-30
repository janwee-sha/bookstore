package com.janwee.bookstore.foundation.web;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class ErrorResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -1598050256955123808L;
    private int status;
    private int detailedStatus;
    private String detailedPhrase;
    private Object error;
    private final LocalDateTime timestamp;
    private String path;

    private ErrorResponse() {
        this.timestamp = LocalDateTime.now();
        this.detailedStatus = 0;
    }

    private ErrorResponse(HttpStatus status) {
        this();
        this.status = status.value();
    }

    private ErrorResponse(ErrorStatus status) {
        this();
        this.status = status.httpStatus().value();
        this.detailedStatus = status.value();
        this.detailedPhrase = status.name();
    }

    public static Builder of(HttpStatus status) {
        return new Builder(status);
    }

    public static Builder of(ErrorStatus status) {
        return new Builder(status);
    }

    public static class Builder implements Serializable {
        @Serial
        private static final long serialVersionUID = -1799966974383264801L;
        private final ErrorResponse response;

        public Builder(HttpStatus status) {
            this.response = new ErrorResponse(status);
        }

        public Builder(ErrorStatus status) {
            this.response = new ErrorResponse(status);
        }

        public Builder withError(Object error) {
            this.response.error = error;
            return this;
        }

        public ErrorResponse build() {
            return response;
        }

        public ErrorResponse underPath(String path) {
            response.path = path;
            return response;
        }
    }
}
