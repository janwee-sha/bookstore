package com.janwee.bookstore.book.infrastructure.persistence.jpa;

import com.janwee.bookstore.book.infrastructure.persistence.entity.AuthorPO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorPOJpaRepository extends JpaRepository<AuthorPO, Long> {
}
