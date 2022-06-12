package com.janwee.bookstore.common.infrastructure.exceptionhandling;

import com.janwee.bookstore.common.domain.exception.HttpException;
import com.janwee.bookstore.common.resource.Error;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<Error> handleHttpException(HttpException e) {
        HttpServletRequest request = Optional.ofNullable(
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        return new ResponseEntity<>(new Error()
                .ofError(e.status().getReasonPhrase())
                .ofMessage(e.getMessage())
                .ofPath(request.getServletPath())
                .ofStatus(e.status()), e.status());
    }
}
