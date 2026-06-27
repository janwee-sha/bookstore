package com.janwee.bookstore.book.infrastructure.persistence.jpa;

import com.janwee.bookstore.book.infrastructure.persistence.entity.BookPO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookPOJpaRepository extends JpaRepository<BookPO, Long> {
}
