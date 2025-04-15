package com.janwee.bookstore.book.infrastructure.persistence;

import com.janwee.bookstore.book.domain.model.Currency;
import com.janwee.bookstore.book.domain.model.Price;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;

@Converter(autoApply = true)
public class PriceConverter implements AttributeConverter<Price, String> {

    @Override
    public String convertToDatabaseColumn(Price price) {
        if (price == null) {
            return null;
        }
        return price.toString();
    }

    @Override
    public Price convertToEntityAttribute(String dbValue) {
        if (dbValue == null) {
            return null;
        }
        String[] values = dbValue.split("/");
        if (values.length != 2) {
            return null;
        }
        return Price.of(Currency.valueOf(values[1]), new BigDecimal(values[0]));
    }
}
