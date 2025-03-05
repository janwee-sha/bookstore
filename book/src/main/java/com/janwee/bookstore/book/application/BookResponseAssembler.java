package com.janwee.bookstore.book.application;

import com.janwee.bookstore.book.domain.model.Author;
import com.janwee.bookstore.book.domain.model.Book;
import com.janwee.bookstore.book.domain.repository.AuthorRepository;
import com.janwee.bookstore.book.presentation.message.BookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BookResponseAssembler {
    private final AuthorRepository authorRepo;

    @Autowired
    public BookResponseAssembler(AuthorRepository authorRepo) {
        this.authorRepo = authorRepo;
    }

    public BookResponse assemble(Book book) {
        BookResponse response = BookResponse.from(book);
        response.setAuthor(authorRepo.findById(book.authorId()).orElse(null));
        return response;
    }

    public List<BookResponse> assemble(List<Book> books) {
        Map<Long, Author> authors = authorRepo.findAllById(
                        books.stream().map(Book::authorId).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(Author::id, Function.identity()));
        return books.stream().map(book -> {
            BookResponse response = BookResponse.from(book);
            response.setAuthor(authors.get(book.authorId()));
            return response;
        }).collect(Collectors.toList());
    }
}
