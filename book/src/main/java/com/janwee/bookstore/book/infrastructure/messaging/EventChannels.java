package com.janwee.bookstore.book.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface EventChannels {
    String ORDER_IN_CHANNEL = "event-in-order";

    @Input(ORDER_IN_CHANNEL)
    MessageChannel orderInputChannel();

    String OUT_CHANNEL = "event-out";

    @Output(OUT_CHANNEL)
    MessageChannel outputChannel();
}
