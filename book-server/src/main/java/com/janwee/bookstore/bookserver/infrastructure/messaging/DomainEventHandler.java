package com.janwee.bookstore.bookserver.infrastructure.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.core.log.LogMessage;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class DomainEventHandler {
//    @Input(Processor.INPUT)
    public void onEvent(LogMessage message) {
        log.info("Received message: {}", message);
    }
}
