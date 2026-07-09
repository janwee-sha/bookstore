package com.janwee.bookstore.foundation.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public abstract class IntegrationEventContractTestSupport {
    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    protected final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(javaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    private static JavaTimeModule javaTimeModule() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FMT));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FMT));
        return module;
    }

    protected void assertNumberField(String payload, String field, long expected) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(payload);
        long actual = node.required(field).asLong();
        if (actual != expected) {
            throw new AssertionError("Expected " + field + " to be " + expected + " but was " + actual);
        }
    }

    protected void assertTextField(String payload, String field, String expected) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(payload);
        String actual = node.required(field).asText();
        if (!Objects.equals(actual, expected)) {
            throw new AssertionError("Expected " + field + " to be '" + expected + "' but was '" + actual + "'");
        }
    }

    protected void assertFieldExists(String payload, String field) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(payload);
        node.required(field);
    }

    protected void assertFieldMissing(String payload, String field) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(payload);
        if (node.has(field)) {
            throw new AssertionError("Expected field '" + field + "' to be missing, but it exists");
        }
    }
}
