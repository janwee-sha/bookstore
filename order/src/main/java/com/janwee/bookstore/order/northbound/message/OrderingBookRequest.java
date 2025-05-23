package com.janwee.bookstore.order.northbound.message;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class OrderingBookRequest {
    @Schema(description = "Book ID")
    @NotNull(message = "Book ID required")
    private Long bookId;

    @Schema(description = "Amount")
    @NotNull(message = "Amount required")
    @Positive(message = "The amount must not be less than 1")
    private int amount;
}
