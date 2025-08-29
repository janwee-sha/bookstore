package com.janwee.bookstore.foundation.web;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * A detailed status for more specific error identification
 *
 * @author Will Hsia
 * @version 1.0
 * @since 2025/8/28
 */
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
     * Status for parameter binding errors,
     * e.g. {@link org.springframework.validation.BindException} encountered
     */
    PARAMETER_BIND_ERROR(HttpStatus.BAD_REQUEST, 4002),
    /**
     * Status for method argument validation errors,
     * e.g. {@link org.springframework.web.bind.MethodArgumentNotValidException} encountered
     */
    ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, 4003),
    /**
     * Status for data integrity violation errors,
     * e.g. {@link java.sql.SQLIntegrityConstraintViolationException},
     * {@link org.springframework.dao.DataIntegrityViolationException} encountered
     */
    DATA_INTEGRITY_VIOLATION(HttpStatus.INTERNAL_SERVER_ERROR, 5001),
    /**
     * Status for generic SQL errors,
     * e.g. {@link java.sql.SQLException} encountered
     */
    SQL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, 5002);

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
