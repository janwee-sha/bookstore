package com.janwee.bookstore.order.northbound.message;

import com.janwee.bookstore.order.domain.State;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -7785686752351423857L;

    private Long id;

    private Long bookId;

    private int amount;

    private LocalDateTime createdAt;

    private State state;
}
