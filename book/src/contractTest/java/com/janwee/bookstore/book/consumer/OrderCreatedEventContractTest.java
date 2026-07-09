package com.janwee.bookstore.book.consumer;

import com.janwee.bookstore.book.interfaces.subscriber.OrderCreated;
import com.janwee.bookstore.foundation.contract.IntegrationEventContractTestSupport;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderCreatedEventContractTest extends IntegrationEventContractTestSupport {

    @Test
    void orderCreatedPublishedByOrderCanBeConsumedByBook() throws Exception {
        com.janwee.bookstore.order.southbound.message.OrderCreated published =
                new com.janwee.bookstore.order.southbound.message.OrderCreated(
                        1001L, 2002L, 3, LocalDateTime.of(2026, 5, 12, 10, 30));

        String payload = objectMapper.writeValueAsString(published);

        assertNumberField(payload, "orderId", 1001L);
        assertNumberField(payload, "bookId", 2002L);
        assertNumberField(payload, "amount", 3);
        assertTextField(payload, "createdBy", "2026-05-12 10:30:00");

        OrderCreated consumed = objectMapper.readValue(payload, OrderCreated.class);
        assertEquals(1001L, consumed.orderId());
        assertEquals(2002L, consumed.bookId());
        assertEquals(3, consumed.amount());
    }

    @Test
    void consumerIgnoresUnknownFieldsFromProvider() throws Exception {
        String payloadWithExtraField = """
                {
                    "orderId": 1001,
                    "bookId": 2002,
                    "amount": 3,
                    "createdBy": "2026-05-12 10:30:00",
                    "newFieldAddedByProvider": "should be ignored"
                }
                """;

        OrderCreated consumed = objectMapper.readValue(payloadWithExtraField, OrderCreated.class);
        assertEquals(1001L, consumed.orderId());
        assertEquals(2002L, consumed.bookId());
        assertEquals(3, consumed.amount());
    }
}
