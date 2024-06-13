package com.janwee.bookstore.book.application;


import com.janwee.bookstore.book.domain.model.Author;
import com.janwee.bookstore.book.domain.repository.AuthorRepository;
import com.janwee.bookstore.book.resource.message.RegisteringAuthorRequest;
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
    public Optional<Author> authorOfId(Long id) {
        log.info("Loading optional author of ID: {}", id);
        return authorRepo.findById(id);
    }

    @Transactional
    public void register(RegisteringAuthorRequest request) {
        log.info("Registering author");
        Author author = request.toAuthor();
        authorRepo.save(author);
    }
}
