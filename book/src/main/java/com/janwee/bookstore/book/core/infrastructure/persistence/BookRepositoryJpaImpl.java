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
    public Page<Book> booksOf(Pageable pageable) {
        return jpaRepo.findAll(pageable);
    }

    @Override
    public Optional<Book> bookOf(Long id) {
        return jpaRepo.findById(id);
    }

    @Override
    public boolean hasBookOf(Long id) {
        return jpaRepo.existsById(id);
    }

    @Override
    public void add(Book book) {
        jpaRepo.save(book);
    }

    @Override
    public void update(Book book) {
        jpaRepo.save(book);
    }

    @Override
    public void delete(Long id) {
        jpaRepo.deleteById(id);
    }
}

