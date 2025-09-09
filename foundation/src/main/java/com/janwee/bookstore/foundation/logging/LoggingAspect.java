package com.janwee.bookstore.foundation.logging;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    private final ObjectMapper objectMapper;

    public LoggingAspect() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    private static final String NON_SERIALIZABLE_ARG = "[Non-serializable argument]";

    @Pointcut("@annotation(Logging) || within(@Logging *)")
    public void loggingPointcut() {
    }

    @Before("loggingPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Bookstore Logging - Calling {}", serializedJoinPoint(joinPoint));
    }

    @AfterReturning("loggingPointcut()")
    public void logAfter(JoinPoint joinPoint) {
        log.info("Bookstore Logging - Exited {}", serializedJoinPoint(joinPoint));
    }

    @AfterThrowing("loggingPointcut()")
    public void logAfterThrowing(JoinPoint joinPoint) {
        log.info("Bookstore Logging - Unexpectedly exited {}", serializedJoinPoint(joinPoint));
    }

    private String serializedJoinPoint(JoinPoint joinPoint) {
        String clazz = joinPoint.getTarget().getClass().getName();
        String method = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        return String.format("%s.%s with arguments %s", clazz, method, serializedArgs(args));
    }

    private String serializedArgs(Object[] args) {
        StringBuilder serializedArgs = new StringBuilder("[");
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            String serializedArg;
            try {
                serializedArg = objectMapper.writeValueAsString(arg);
            } catch (JsonProcessingException e) {
                log.warn("Failed to serialize argument", e);
                serializedArg = NON_SERIALIZABLE_ARG;
            }
            serializedArgs.append(serializedArg);
            if (i < args.length - 1) {
                serializedArgs.append(", ");
            }
        }
        serializedArgs.append("]");
        return serializedArgs.toString();
    }
}
