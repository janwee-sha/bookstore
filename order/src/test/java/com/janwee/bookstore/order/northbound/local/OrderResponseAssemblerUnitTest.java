package com.janwee.bookstore.order.northbound.local;

import com.janwee.bookstore.order.domain.Order;
import com.janwee.bookstore.order.domain.State;
import com.janwee.bookstore.order.northbound.message.OrderResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderResponseAssemblerUnitTest {
    @Test
    void shouldMapOrderToResponse() {
        LocalDateTime createdAt = LocalDateTime.now();
        Order order = new Order(1L, 2L, 3, createdAt, State.APPROVAL_PENDING);

        OrderResponse response = OrderResponseAssembler.from(order);

        assertAll(
                () -> assertEquals(1L, response.getId()),
                () -> assertEquals(2L, response.getBookId()),
                () -> assertEquals(3, response.getAmount()),
                () -> assertEquals(createdAt, response.getCreatedAt()),
                () -> assertEquals(State.APPROVAL_PENDING, response.getState())
        );
    }

    @Test
    void shouldReturnNullForNullOrder() {
        assertNull(OrderResponseAssembler.from(null));
    }
}
