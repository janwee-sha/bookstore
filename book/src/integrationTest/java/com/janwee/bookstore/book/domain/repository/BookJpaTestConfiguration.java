package com.janwee.bookstore.book.domain.repository;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EntityScan("com.janwee.bookstore.book.core.domain.model")
@EnableJpaRepositories("com.janwee.bookstore.book.core.domain.repository")
class BookJpaTestConfiguration {
}
