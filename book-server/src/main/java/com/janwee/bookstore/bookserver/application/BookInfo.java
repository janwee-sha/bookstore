package com.janwee.bookstore.bookserver.application;

import com.janwee.bookstore.bookserver.domain.Author;
import com.janwee.bookstore.bookserver.domain.Book;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;

public class BookInfo {
    private Long id;
    private String name;
    private LocalDate publishBy;
    private String publisher;
    private Author author;

    public BookInfo() {
    }

    public BookInfo(Book book, Author author) {
        if (book != null) {
            BeanUtils.copyProperties(book, this);
        }
        if (author != null) {
            this.author = author;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getPublishBy() {
        return publishBy;
    }

    public void setPublishBy(LocalDate publishBy) {
        this.publishBy = publishBy;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
