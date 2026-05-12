package com.janwee.bookstore.book.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageContractTest {
    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(javaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    @Test
    void orderCreatedPublishedByOrderCanBeConsumedByBook() throws Exception {
        com.janwee.bookstore.order.core.southbound.message.OrderCreated published = new
                com.janwee.bookstore.order.core.southbound.message.OrderCreated(1001L, 2002L, 3,
                LocalDateTime.of(2026, 5, 12, 10, 30));

        String payload = objectMapper.writeValueAsString(published);

        assertNumberField(payload, "orderId", 1001L);
        assertNumberField(payload, "bookId", 2002L);
        assertNumberField(payload, "amount", 3);
        assertTextField(payload, "createdBy", "2026-05-12 10:30:00");

        com.janwee.bookstore.book.core.domain.event.OrderCreated consumed = objectMapper.readValue(
                payload, com.janwee.bookstore.book.core.domain.event.OrderCreated.class);
        assertEquals(1001L, consumed.orderId());
        assertEquals(2002L, consumed.bookId());
        assertEquals(3, consumed.amount());
    }

    private static JavaTimeModule javaTimeModule() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FMT));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FMT));
        return module;
    }

    private void assertNumberField(String payload, String field, long expected) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(payload);
        assertEquals(expected, node.required(field).asLong());
    }

    private void assertTextField(String payload, String field, String expected) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(payload);
        assertEquals(expected, node.required(field).asText());
    }
}
