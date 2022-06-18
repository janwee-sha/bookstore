package com.janwee.bookstore.bookserver.infrastructure.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableBinding(Processor.class)
public class DomainEventHandler {
    @StreamListener(Sink.INPUT)
    public void onText(String message) {
        log.info("Received message: {}", message);
    }
}
