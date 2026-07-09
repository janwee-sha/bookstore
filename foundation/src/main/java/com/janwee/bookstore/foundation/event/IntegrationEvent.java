package com.janwee.bookstore.foundation.event;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface IntegrationEvent extends Serializable {
    LocalDateTime occurredAt();
}
