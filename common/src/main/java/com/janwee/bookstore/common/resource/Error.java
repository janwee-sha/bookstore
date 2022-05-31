package com.janwee.bookstore.common.resource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
public class Error {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String error;
    private String message;
    private String detail;
    private String path;

    public Error() {
        this.timestamp = LocalDateTime.now();
    }

    public Error ofStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public Error ofError(String error) {
        this.error = error;
        return this;
    }

    public Error ofMessage(String message) {
        this.message = message;
        return this;
    }

    public Error ofDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public Error ofPath(String path) {
        this.path = path;
        return this;
    }

    public Integer getStatus() {
        return Optional.ofNullable(status).map(HttpStatus::value).orElse(null);
    }
}
