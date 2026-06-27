package com.janwee.bookstore.book.infrastructure.persistence.assembler;

import com.janwee.bookstore.book.domain.model.Author;
import com.janwee.bookstore.book.infrastructure.persistence.entity.AuthorPO;

public class AuthorPOAssembler {
    public static Author toDomain(AuthorPO po) {
        if (po == null) {
            return null;
        }

        return new Author(
                po.getId(),
                po.getName(),
                po.getProfile(),
                po.getPhoneNumber()
        );
    }

    public static AuthorPO toPO(Author author) {
        if (author == null) {
            return null;
        }

        return new AuthorPO(
                author.id(),
                author.name(),
                author.profile(),
                author.phoneNumber()
        );
    }
}
