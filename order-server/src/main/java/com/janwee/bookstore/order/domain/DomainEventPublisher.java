package com.janwee.bookstore.order.domain;

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
    private final EventChannels eventChannels;

    @Autowired
    public DomainEventPublisher(EventChannels eventChannels) {
        this.eventChannels = eventChannels;
    }

    public void publish(String eventType, DomainEvent... events) {
        MessageHeaders headers = new MessageHeaders(new HashMap<String, Object>() {
            private static final long serialVersionUID = -4295001798254124290L;

            {
                put("type", eventType);
            }
        });
        Stream.of(events).parallel().forEach(event -> {
            eventChannels.eventOutput().send(MessageBuilder.createMessage(event, headers));
            log.info("Published {} event: {}", eventType, event);
        });
    }
}
