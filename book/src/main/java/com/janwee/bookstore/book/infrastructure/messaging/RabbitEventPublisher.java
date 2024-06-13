package com.janwee.bookstore.book.infrastructure.messaging;

import com.janwee.bookstore.foundation.event.Event;
import com.janwee.bookstore.foundation.event.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.stream.Stream;

@Component
@Slf4j
public class RabbitEventPublisher implements EventPublisher {
    private final EventChannels eventChannels;

    @Autowired
    public RabbitEventPublisher(EventChannels eventChannels) {
        this.eventChannels = eventChannels;
    }

    @Override
    public void publish(String eventType, Event... events) {
        MessageHeaders headers = new MessageHeaders(new HashMap<>() {{
            put("type", eventType);
        }});
        Stream.of(events).parallel().forEach(event -> {
            eventChannels.outputChannel().send(MessageBuilder.createMessage(event, headers));
            log.info("Published {} event: {}", eventType, event);
        });
    }
}
