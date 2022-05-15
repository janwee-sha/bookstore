package com.janwee.bookstore.common.domain.exception;

import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException {
    private HttpStatus status;

    public HttpException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    public HttpException(Throwable cause, HttpStatus status) {
        super(cause);
        this.status = status;
    }

    public HttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, HttpStatus status) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.status = status;
    }

    public HttpStatus status() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}