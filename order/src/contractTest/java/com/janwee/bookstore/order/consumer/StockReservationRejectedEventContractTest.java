package com.janwee.bookstore.order.consumer;

import com.janwee.bookstore.foundation.contract.IntegrationEventContractTestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StockReservationRejectedEventContractTest extends IntegrationEventContractTestSupport {

    @Test
    void stockReservationRejectedPublishedByBookCanBeConsumedByOrder() throws Exception {
        com.janwee.bookstore.book.application.event.StockReservationRejected published =
                new com.janwee.bookstore.book.application.event.StockReservationRejected(1001L, 2002L);

        String payload = objectMapper.writeValueAsString(published);

        assertNumberField(payload, "orderId", 1001L);
        assertNumberField(payload, "bookId", 2002L);

        com.janwee.bookstore.order.northbound.message.StockReservationRejected consumed =
                objectMapper.readValue(payload, com.janwee.bookstore.order.northbound.message.StockReservationRejected.class);
        assertEquals(1001L, consumed.orderId());
        assertEquals(2002L, consumed.bookId());
    }

    @Test
    void consumerIgnoresUnknownFieldsFromProvider() throws Exception {
        String payloadWithExtraField = """
                {
                    "orderId": 1001,
                    "bookId": 2002,
                    "newFieldAddedByProvider": "should be ignored"
                }
                """;

        com.janwee.bookstore.order.northbound.message.StockReservationRejected consumed =
                objectMapper.readValue(payloadWithExtraField,
                        com.janwee.bookstore.order.northbound.message.StockReservationRejected.class);
        assertEquals(1001L, consumed.orderId());
        assertEquals(2002L, consumed.bookId());
    }
}
