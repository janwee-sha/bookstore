package com.janwee.bookstore.foundation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class DomainExceptionHandler {

    @ExceptionHandler(value = {BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Error handleBadRequestExceptions(Exception e) {
        log.info(e.getMessage(), e);
        HttpServletRequest request = Optional.ofNullable(
                        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        return new Error()
                .ofStatus(HttpStatus.BAD_REQUEST)
                .ofMessage(e.getMessage())
                .ofPath(request.getServletPath());
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected Error handleNotFoundExceptions(Exception e) {
        log.error(e.getMessage(), e);
        HttpServletRequest request = Optional.ofNullable(
                        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        return new Error()
                .ofStatus(HttpStatus.NOT_FOUND)
                .ofMessage(e.getMessage())
                .ofPath(request.getServletPath());
    }
}
