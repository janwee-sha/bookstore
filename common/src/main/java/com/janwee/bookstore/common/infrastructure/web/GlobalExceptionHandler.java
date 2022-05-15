package com.janwee.bookstore.common.infrastructure.web;

import com.janwee.bookstore.common.domain.exception.HttpException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestControllerAdvice(basePackages = "com.janwee.bookstore")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<Result> handleHttpException(HttpException e) {
        HttpServletRequest request = Optional.ofNullable(
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        return new ResponseEntity<>(new Result().ofError(e.status().getReasonPhrase())
                .ofMessage(e.getMessage()).ofPath(request.getServletPath()).ofStatus(e.status()), e.status());
    }
}
