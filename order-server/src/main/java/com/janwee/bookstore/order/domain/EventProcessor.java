package com.janwee.bookstore.order.domain;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface EventProcessor {
    String eventOutput = "event-out";
    String eventFromBook = "event-in-book";

    @Output(eventOutput)
    MessageChannel eventOutput();

    @Input(eventFromBook)
    MessageChannel eventFromBook();
}
