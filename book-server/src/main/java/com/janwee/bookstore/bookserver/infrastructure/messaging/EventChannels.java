package com.janwee.bookstore.bookserver.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface EventChannels {
    String eventFromOrder = "event-in-order";

    @Input(eventFromOrder)
    MessageChannel eventFromOrder();

    String eventOutput = "event-out";

    @Output(eventOutput)
    MessageChannel eventOutput();
}
