package com.janwee.bookstore.foundation.event;

import java.time.LocalDateTime;

/**
 * TODO Add a description for the class here
 *
 * @author Will Hsia
 * @since 2024/6/13
 */
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
