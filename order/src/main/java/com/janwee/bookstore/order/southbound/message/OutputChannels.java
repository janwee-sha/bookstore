package com.janwee.bookstore.order.southbound.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface OutputChannels {
    String OUT_CHANNEL = "event-out";

    @Output(OUT_CHANNEL)
    MessageChannel outputChannel();
}
