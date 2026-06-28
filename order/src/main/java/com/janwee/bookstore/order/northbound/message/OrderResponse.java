package com.janwee.bookstore.order.northbound.message;

import com.janwee.bookstore.order.domain.State;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(title = "Order", description = "Order details")
public class OrderResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -7785686752351423857L;

    @Schema(title = "ID", description = "Order ID", example = "1")
    private Long id;

    @Schema(title = "Book ID", description = "Ordered book ID", example = "10")
    private Long bookId;

    @Schema(title = "Amount", description = "Number of books ordered", example = "2")
    private int amount;

    @Schema(title = "Created At", description = "Order creation time", example = "2026-06-28T10:15:30")
    private LocalDateTime createdAt;

    @Schema(title = "State", description = "Order state", example = "APPROVAL_PENDING",
            allowableValues = {"APPROVAL_PENDING", "APPROVED", "REJECTED"})
    private State state;
}
