package com.janwee.bookstore.book.application.assembler;

import com.janwee.bookstore.book.application.view.AuthorView;
import com.janwee.bookstore.book.application.view.BookView;
import com.janwee.bookstore.book.domain.model.Author;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BookViewAssembler {
    private final AuthorRepository authorRepo;

    @Autowired
    public BookViewAssembler(AuthorRepository authorRepo) {
        this.authorRepo = authorRepo;
    }

    public BookView assemble(Book book) {
        BookView response = BookView.from(book);
        response.setAuthor(AuthorView.from(authorRepo.authorOf(book.authorId()).orElse(null)));
        return response;
    }

    public List<BookView> assemble(List<Book> books) {
        Map<Long, Author> authors = authorRepo.authorsOf(
                        books.stream().map(Book::authorId).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(Author::id, Function.identity()));
        return books.stream().map(book -> {
            BookView response = BookView.from(book);
            response.setAuthor(AuthorView.from(authors.get(book.authorId())));
            return response;
        }).collect(Collectors.toList());
    }
}
