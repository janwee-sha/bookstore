package com.janwee.bookstore.book.domain.repository;

import com.janwee.bookstore.book.infrastructure.persistence.AuthorRepositoryJpaAdapter;
import com.janwee.bookstore.book.infrastructure.persistence.BookRepositoryJpaAdapter;
import com.janwee.bookstore.book.infrastructure.persistence.InventoryItemRepositoryJpaAdapter;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EntityScan({
        "com.janwee.bookstore.book.infrastructure.persistence.entity"
})
@EnableJpaRepositories("com.janwee.bookstore.book.infrastructure.persistence.jpa")
@Import({AuthorRepositoryJpaAdapter.class, BookRepositoryJpaAdapter.class, InventoryItemRepositoryJpaAdapter.class})
class BookJpaTestConfiguration {
}
