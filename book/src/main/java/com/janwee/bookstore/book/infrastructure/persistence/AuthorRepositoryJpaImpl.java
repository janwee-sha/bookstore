package com.janwee.bookstore.book.infrastructure.persistence;

import com.janwee.bookstore.book.domain.model.Author;
import com.janwee.bookstore.book.domain.repository.AuthorRepository;
import com.janwee.bookstore.book.infrastructure.persistence.assembler.AuthorPOAssembler;
import com.janwee.bookstore.book.infrastructure.persistence.entity.AuthorPO;
import com.janwee.bookstore.book.infrastructure.persistence.jpa.AuthorPOJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryJpaImpl implements AuthorRepository {
    private final AuthorPOJpaRepository jpaRepo;

    @Override
    public Optional<Author> authorOf(Long id) {
        return jpaRepo.findById(id)
                .map(AuthorPOAssembler::toDomain);
    }

    @Override
    public List<Author> authorsOf(Collection<Long> ids) {
        return jpaRepo.findAllById(ids)
                .stream()
                .map(AuthorPOAssembler::toDomain)
                .toList();
    }

    @Override
    public boolean hasAuthorOf(Long id) {
        return jpaRepo.existsById(id);
    }

    @Override
    public void add(Author author) {
        Assert.notNull(author, "Author is required");
        Assert.isNull(author.id(), "New author must not already have an ID");
        AuthorPO saved = jpaRepo.save(AuthorPOAssembler.toPO(author));
        author.assignId(saved.getId());
    }

    @Override
    public void update(Author author) {
        Assert.notNull(author, "Author is required");
        Long id = author.id();
        Assert.notNull(id, "Existing author ID is required for update");
        Assert.isTrue(jpaRepo.existsById(id), "Existing author must be present before update");
        jpaRepo.save(AuthorPOAssembler.toPO(author));
    }
}
