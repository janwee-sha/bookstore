package com.janwee.bookstore.bookserver.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_book")
public class Book implements Serializable {
    @Id
    @GeneratedValue(generator = "tbl_book_id_seq")
    private Long id;

    @Column(name = "book_name", nullable = false)
    private String name;

    @Column(name = "publish_by", nullable = false)
    private LocalDate publishBy;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "author_id", nullable = false)
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
