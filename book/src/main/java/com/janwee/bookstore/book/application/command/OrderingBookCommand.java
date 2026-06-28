package com.janwee.bookstore.book.application.command;


import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderingBookCommand {
    private long bookId;

    private long orderId;

    @Positive(message = "The amount must not be less than 1")
    private int amount;
}
