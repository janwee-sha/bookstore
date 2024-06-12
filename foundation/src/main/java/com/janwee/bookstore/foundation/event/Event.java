package com.janwee.bookstore.foundation.event;

import java.time.LocalDateTime;

public interface Event {
    String description();
    LocalDateTime occurredAt();
}
