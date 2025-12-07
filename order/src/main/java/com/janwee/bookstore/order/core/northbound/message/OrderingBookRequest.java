package com.janwee.bookstore.order.core.northbound.message;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderingBookRequest {
    @Schema(description = "Book ID")
    private long bookId;

    @Schema(description = "Amount")
    @Positive(message = "The amount must not be less than 1")
    private int amount;
}
