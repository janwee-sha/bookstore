package com.janwee.bookstore.bookserver.domain;

import java.io.Serializable;
import java.time.LocalDate;

public class Book implements Serializable {
    private Long id;
    private String name;
    private LocalDate publishBy;
    private String publisher;
    private Long authorId;

    public Book() {
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

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
