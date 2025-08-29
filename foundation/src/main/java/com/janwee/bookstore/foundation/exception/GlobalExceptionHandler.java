package com.janwee.bookstore.foundation.exception;

import com.janwee.bookstore.foundation.web.ErrorResponse;
import com.janwee.bookstore.foundation.web.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse handleNotFoundExceptions(Exception e) {
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

    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleParameterBindExceptions(BindException e) {
        log.warn(e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        return ErrorResponse.of(ErrorStatus.PARAMETER_BIND_ERROR)
                .withError(extractBindingResultErrors(bindingResult))
                .underPath(path());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentValidationExceptions(MethodArgumentNotValidException e) {
        log.warn(e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        return ErrorResponse.of(ErrorStatus.ARGUMENT_NOT_VALID)
                .withError(extractBindingResultErrors(bindingResult))
                .underPath(path());
    }

    private static Map<String, String> extractBindingResultErrors(BindingResult bindingResult) {
        return bindingResult.hasErrors()
                ? bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage())
                                .orElse(DEFAULT_FIELD_VALID_MSG)))
                : null;
    }

    //    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleDataIntegrationViolationExceptions(Exception e) {
        log.error(e.getMessage(), e);
        return ErrorResponse.of(ErrorStatus.DATA_INTEGRITY_VIOLATION)
                .withError(e.getMessage())
                .underPath(path());
    }

    //    @ExceptionHandler({SQLException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSqlExceptionHelper(Exception e) {
        log.error(e.getMessage(), e);
        return ErrorResponse.of(ErrorStatus.SQL_EXCEPTION)
                .withError(e.getMessage())
                .underPath(path());
    }

    private String path() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .map(HttpServletRequest::getServletPath)
                .orElse("/unknown");
    }
}
