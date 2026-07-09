package com.janwee.bookstore.book.application.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.janwee.bookstore.foundation.event.DefaultIntegrationEvent;
import com.janwee.bookstore.foundation.event.IntegrationEvent;

import java.io.Serial;

public class StockReservationConfirmed extends DefaultIntegrationEvent implements IntegrationEvent {
    @Serial
    private static final long serialVersionUID = -8710269418141302043L;

    @JsonProperty
    private final long orderId;

    @JsonProperty
    private final long bookId;

    @JsonCreator
    public StockReservationConfirmed(@JsonProperty("orderId") long orderId,
                                     @JsonProperty("bookId") long bookId) {
        this.orderId = orderId;
        this.bookId = bookId;
    }

    public long orderId() {
        return orderId;
    }

    public long bookId() {
        return bookId;
    }
}
