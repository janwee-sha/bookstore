package com.janwee.bookstore.author.application;


import com.janwee.bookstore.author.domain.Author;
import com.janwee.bookstore.author.domain.AuthorRepository;
import com.janwee.bookstore.author.domain.TelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class AuthorApplicationService {
    private final TelService telService;
    private final AuthorRepository authorRepo;

    @Autowired
    public AuthorApplicationService(TelService telService, AuthorRepository authorRepo) {
        this.telService = telService;
        this.authorRepo = authorRepo;
    }

    @Transactional(readOnly = true)
    public Optional<Author> author(Long id) {
        log.info("Loading optional author of ID: {}", id);
        return authorRepo.findById(id);
    }

    @Transactional
    public void register(Author author) {
        log.info("Registering author");
        author.setId(null);
        telService.registerTel(author.getId(), author.getPhoneNumber());
        authorRepo.save(author);
    }
}
