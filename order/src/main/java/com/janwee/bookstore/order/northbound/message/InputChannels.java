package com.janwee.bookstore.order.northbound.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

/**
 * TODO Add a description for the class here
 *
 * @author Will Hsia
 * @since 2024/6/13
 */
public interface InputChannels {
    String BOOK_IN_CHANNEL = "event-in-book";
    @Input(BOOK_IN_CHANNEL)
    MessageChannel bookInputChannel();
}
