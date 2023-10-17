package com.janwee.bookstore.order.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface EventChannels {
    String eventOutput = "event-out";
    String eventFromBook = "event-in-book";

    @Output(eventOutput)
    MessageChannel eventOutput();

    @Input(eventFromBook)
    MessageChannel eventFromBook();
}
