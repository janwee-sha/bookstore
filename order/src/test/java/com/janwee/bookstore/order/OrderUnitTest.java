package com.janwee.bookstore.order;

import com.janwee.bookstore.order.core.domain.InvalidOrderException;
import com.janwee.bookstore.order.core.domain.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderUnitTest {
    @Test
    void createOrderWithNonPositiveAmountShouldThrowException() {
        assertThrows(InvalidOrderException.class, () -> Order.create().ofAmount(0));
        assertThrows(InvalidOrderException.class, () -> Order.create().ofAmount(-1));
    }
}
