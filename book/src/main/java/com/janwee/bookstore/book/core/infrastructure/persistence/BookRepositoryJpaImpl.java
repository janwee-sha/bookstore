package com.janwee.bookstore.book.core.infrastructure.persistence;

import com.janwee.bookstore.book.core.domain.model.Book;
import com.janwee.bookstore.book.core.domain.repository.BookRepository;
import com.janwee.bookstore.book.core.infrastructure.persistence.jpa.BookJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJpaImpl implements BookRepository {
    private final BookJpaRepository jpaRepo;

    @Override
    public Page<Book> findAll(Pageable pageable) {
        return jpaRepo.findAll(pageable);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return jpaRepo.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepo.existsById(id);
    }

    @Override
    public Book save(Book book) {
        return jpaRepo.save(book);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepo.deleteById(id);
    }
}

