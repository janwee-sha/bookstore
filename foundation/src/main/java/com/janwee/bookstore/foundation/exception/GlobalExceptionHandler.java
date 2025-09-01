package com.janwee.bookstore.foundation.exception;

import com.janwee.bookstore.foundation.web.ErrorResponse;
import com.janwee.bookstore.foundation.web.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String DEFAULT_FIELD_VALID_MSG = "is not valid";

    @ExceptionHandler(value = {BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleBadRequestExceptions(BadRequestException e) {
        log.warn(e.getMessage(), e);
        return ErrorResponse.of(ErrorStatus.BAD_DOMAIN_REQUEST)
                .withError(e.getMessage())
                .underPath(path());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingParameterExceptions(MissingServletRequestParameterException e) {
        log.warn(e.getMessage(), e);
        return ErrorResponse.of(ErrorStatus.PARAMETER_REQUIRED)
                .withError(e.getParameterName() + " is required")
                .underPath(path());
    }

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            MethodArgumentConversionNotSupportedException.class,
            ConversionNotSupportedException.class,
            BindException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleParameterTypeMismatch(Exception e) {
        log.warn("Parameter type mismatch: {}", e.getMessage(), e);
        if (e instanceof BindException be) {
            return ErrorResponse.of(ErrorStatus.PARAMETER_TYPE_MISMATCH)
                    .withError(extractBindingResultErrors(be.getBindingResult()))
                    .underPath(path());
        }
        return ErrorResponse.of(ErrorStatus.PARAMETER_TYPE_MISMATCH)
                .withError(e.getMessage())
                .underPath(path());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleParameterConstraintViolation(ConstraintViolationException e) {
        log.warn("Parameter constraint violation: {}", e.getMessage(), e);
        Map<String, String> errors = e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        cv -> {
                            String path = cv.getPropertyPath().toString();
                            return path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
                        },
                        cv -> Optional.ofNullable(cv.getMessage()).orElse(DEFAULT_FIELD_VALID_MSG)
                ));
        return ErrorResponse.of(ErrorStatus.PARAMETER_CONSTRAINT_VIOLATION)
                .withError(errors)
                .underPath(path());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRequestBodyConversion(HttpMessageNotReadableException e) {
        log.warn("Request body not readable: {}", e.getMessage(), e);
        return ErrorResponse.of(ErrorStatus.BODY_MESSAGE_NOT_READABLE)
                .withError(e.getMostSpecificCause().getMessage())
                .underPath(path());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRequestBodyConstraintViolation(MethodArgumentNotValidException e) {
        log.warn("Request body constraint violation: {}", e.getMessage(), e);
        return ErrorResponse.of(ErrorStatus.BODY_CONSTRAINT_VIOLATION)
                .withError(extractBindingResultErrors(e.getBindingResult()))
                .underPath(path());
    }

    @ExceptionHandler({PropertyReferenceException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handlePropertyReferenceExceptions(PropertyReferenceException e) {
        log.warn(e.getMessage(), e);
        return ErrorResponse.of(ErrorStatus.INVALID_PROPERTY_REFERENCE)
                .withError(e.getMessage())
                .underPath(path());
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse handleNotFoundExceptions(Exception e) {
        log.warn(e.getMessage(), e);
        return ErrorResponse.of(HttpStatus.NOT_FOUND)
                .withError(e.getMessage())
                .underPath(path());
    }

    private String path() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .map(HttpServletRequest::getServletPath)
                .orElse("/unknown");
    }

    private static Map<String, String> extractBindingResultErrors(BindingResult bindingResult) {
        return bindingResult.hasErrors()
                ? bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage())
                                .orElse(DEFAULT_FIELD_VALID_MSG)))
                : null;
    }
}
