package com.janwee.bookstore.foundation.web;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorStatus {
    /**
     * Status for generic bad requests
     */
    BAD_DOMAIN_REQUEST(HttpStatus.BAD_REQUEST, 4000),
    /**
     * Status for missing required parameters,
     * e.g. {@link org.springframework.web.bind.MissingServletRequestParameterException} encountered
     */
    PARAMETER_REQUIRED(HttpStatus.BAD_REQUEST, 4001),
    /**
     * Status for request parameter/path variable type mismatch errors,
     */
    PARAMETER_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, 4002),
    /**
     * Status for request parameter/path variable constraint violation errors,
     */
    PARAMETER_CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, 4003),
    /**
     * Status for unreadable request body,
     */
    BODY_MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, 4004),
    /**
     * Status for request body constraint violation errors,
     */
    BODY_CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, 4005),

    /**
     * Status for invalid property reference of models,
     * e.g. {@link org.springframework.data.mapping.PropertyReferenceException} encountered
     */
    INVALID_PROPERTY_REFERENCE(HttpStatus.BAD_REQUEST, 4006);

    private final HttpStatus httpStatus;

    private final int value;


    ErrorStatus(HttpStatus httpStatus, int value) {
        this.httpStatus = httpStatus;
        this.value = value;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }

    public int value() {
        return value;
    }
}
