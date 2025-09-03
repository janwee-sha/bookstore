package com.janwee.bookstore.foundation.event;

import java.io.Serial;
import java.time.LocalDateTime;

public abstract class DomainEvent implements Event{
    @Serial
    private static final long serialVersionUID = 3899524081496136383L;

    private final LocalDateTime occurredAt;

    protected DomainEvent() {
        this.occurredAt = LocalDateTime.now();
    }

    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }
}
