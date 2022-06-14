package com.janwee.bookstore.authorserver.application;


import com.janwee.bookstore.authorserver.domain.Author;
import com.janwee.bookstore.authorserver.domain.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class AuthorApplicationService {
    private final AuthorRepository authorRepo;

    @Autowired
    public AuthorApplicationService(AuthorRepository authorRepo) {
        this.authorRepo = authorRepo;
    }

    @Transactional(readOnly = true)
    public Optional<Author> author(Long id) {
        log.info("Loading optional author of ID: {}", id);
        return authorRepo.findById(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void register(Author author) {
        log.info("Registering author");
        author.setId(null);
        authorRepo.save(author);
    }
}
