package com.janwee.bookstore.bookserver.infrastructure.messaging;

import com.janwee.bookstore.bookserver.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.stream.Stream;

@Component
@Slf4j
public class EventPublisher {
    private final EventChannels eventChannels;

    @Autowired
    public EventPublisher(EventChannels eventChannels) {
        this.eventChannels = eventChannels;
    }

    public void publish(String eventType, Event... events) {
        MessageHeaders headers = new MessageHeaders(new HashMap<String, Object>() {{
            put("type", eventType);
        }});
        Stream.of(events).parallel().forEach(event -> {
            eventChannels.eventOutput().send(MessageBuilder.createMessage(event, headers));
            log.info("Published {} event: {}", eventType, event);
        });
    }
}
