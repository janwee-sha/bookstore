package com.janwee.bookstore.book.infrastructure.persistence.assembler;

import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.model.Currency;
import com.janwee.bookstore.book.domain.model.Price;
import com.janwee.bookstore.book.infrastructure.persistence.entity.BookPO;

import java.math.BigDecimal;

public class BookPOAssembler {
    public static Book toDomain(BookPO po) {
        if (po == null) {
            return null;
        }

        return new Book(
                po.getId(),
                po.getName(),
                po.getAmount(),
                toPrice(po.getPrice()),
                po.getPublishedAt(),
                po.getPublisher(),
                po.getAuthorId()
        );
    }

    public static BookPO toPO(Book book) {
        if (book == null) {
            return null;
        }

        return new BookPO(
                book.id(),
                book.name(),
                book.amount(),
                toPriceValue(book.price()),
                book.publishedAt(),
                book.publisher(),
                book.authorId());
    }

    private static String toPriceValue(Price price) {
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        return price.toString();
    }

    private static Price toPrice(String dbValue) {
        if (dbValue == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        String[] values = dbValue.split("/");
        if (values.length != 2) {
            throw new IllegalArgumentException("Price is not a valid price");
        }
        return Price.of(Currency.valueOf(values[1]), new BigDecimal(values[0]));
    }
}
