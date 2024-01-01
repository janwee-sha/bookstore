package com.janwee.bookstore.order.infrastructure.messaging;

import com.janwee.bookstore.order.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
        List<CorrelationData> results = new ArrayList<>();
        Stream.of(events).parallel()
                .forEach(event -> {
                    CorrelationData correlation = new CorrelationData(UUID.randomUUID().toString());
                    MessageHeaders headers = new MessageHeaders(new HashMap<String, Object>() {
                        private static final long serialVersionUID = -4295001798254124290L;

                        {
                            put("type", eventType);
                            put(AmqpHeaders.CORRELATION_ID, correlation);
                        }
                    });
                    results.add(correlation);
                    eventChannels.eventOutput().send(MessageBuilder.createMessage(event, headers));
                    log.info("Published {} event: {}", eventType, event);
                });
        results.forEach(correlation -> {
            try {
                CorrelationData.Confirm confirm = correlation.getFuture().get(10, TimeUnit.SECONDS);
                log.info(confirm + " for " + correlation.getId());
                if (correlation.getReturnedMessage() != null) {
                    log.error("Message for " + correlation.getId() + " was returned ");

                    // throw some exception to invoke binder retry/error handling

                }
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            }
            catch (ExecutionException | TimeoutException e) {
                throw new IllegalStateException(e);
            }
        });
    }
}
