package com.janwee.bookstore.order.northbound.message;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class OrderingRequest {
    @Schema(description = "Book ID")
    @NotNull(message = "Book ID required")
    private Long bookId;

    @Schema(description = "Amount")
    @NotNull(message = "Amount required")
    @Positive(message = "The amount must not be less than 1")
    private int amount;
}
