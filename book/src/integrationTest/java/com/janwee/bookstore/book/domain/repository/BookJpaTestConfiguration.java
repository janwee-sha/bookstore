package com.janwee.bookstore.book.domain.repository;

import com.janwee.bookstore.book.infrastructure.persistence.AuthorRepositoryJpaImpl;
import com.janwee.bookstore.book.infrastructure.persistence.BookRepositoryJpaImpl;
import com.janwee.bookstore.book.infrastructure.persistence.InventoryItemRepositoryJpaImpl;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EntityScan({
        "com.janwee.bookstore.book.infrastructure.persistence.entity"
})
@EnableJpaRepositories("com.janwee.bookstore.book.infrastructure.persistence.jpa")
@Import({AuthorRepositoryJpaImpl.class, BookRepositoryJpaImpl.class, InventoryItemRepositoryJpaImpl.class})
class BookJpaTestConfiguration {
}
