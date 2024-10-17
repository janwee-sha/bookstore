package com.janwee.bookstore.foundation.event;

import java.time.LocalDateTime;

public interface Event {
    LocalDateTime occurredAt();
}
