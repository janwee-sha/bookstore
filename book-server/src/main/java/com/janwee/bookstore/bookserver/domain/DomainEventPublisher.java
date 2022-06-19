package com.janwee.bookstore.bookserver.domain;

import com.janwee.bookstore.common.domain.event.DomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.stream.Stream;

@Component
@Slf4j
public class DomainEventPublisher {
    private final EventProcessor eventProcessor;

    @Autowired
    public DomainEventPublisher(EventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    public void publish(String eventType, DomainEvent... events) {
        MessageHeaders headers = new MessageHeaders(new HashMap<String, Object>() {{
            put("type", eventType);
        }});
        Stream.of(events).parallel().forEach(event -> {
            eventProcessor.eventOutput().send(MessageBuilder.createMessage(event, headers));
            log.info("Published {} event: {}", eventType, event);
        });
    }
}
