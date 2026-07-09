package com.janwee.bookstore.foundation.event;

import java.io.Serial;
import java.time.LocalDateTime;

public abstract class DefaultIntegrationEvent implements IntegrationEvent {
    @Serial
    private static final long serialVersionUID = 3899524081496136383L;

    private final LocalDateTime occurredAt;

    protected DefaultIntegrationEvent() {
        this.occurredAt = LocalDateTime.now();
    }

    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }
}
