package com.janwee.bookstore.book.application.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Schema(title = "Adjusting Stock Request")
public class AdjustingStockCommand implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "New Quantity")
    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity should not be negative")
    private Integer quantity;
}
