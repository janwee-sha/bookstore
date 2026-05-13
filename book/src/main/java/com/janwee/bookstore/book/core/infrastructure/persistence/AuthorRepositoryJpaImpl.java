package com.janwee.bookstore.book.core.infrastructure.persistence;

import com.janwee.bookstore.book.core.domain.model.Author;
import com.janwee.bookstore.book.core.domain.repository.AuthorRepository;
import com.janwee.bookstore.book.core.infrastructure.persistence.jpa.AuthorJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryJpaImpl implements AuthorRepository {
    private final AuthorJpaRepository jpaRepo;

    @Override
    public Optional<Author> authorOf(Long id) {
        return jpaRepo.findById(id);
    }

    @Override
    public List<Author> authorsOf(Collection<Long> ids) {
        return jpaRepo.findAllById(ids);
    }

    @Override
    public boolean hasAuthorOf(Long id) {
        return jpaRepo.existsById(id);
    }

    @Override
    public void add(Author author) {
        jpaRepo.save(author);
    }

    @Override
    public void update(Author author) {
        jpaRepo.save(author);
    }
}

