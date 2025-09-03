package com.janwee.bookstore.foundation.event;

import java.time.LocalDateTime;

public abstract class DomainEvent implements Event{
    private final LocalDateTime occurredAt;

    protected DomainEvent() {
        this.occurredAt = LocalDateTime.now();
    }

    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }
}
