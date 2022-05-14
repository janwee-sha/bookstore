package com.janwee.bookstore.bookserver.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tbl_book")
public class Book implements Serializable {
    @Id
    @GeneratedValue(generator = "tbl_book_id_seq")
    private Long id;

    @NotBlank
    @Column(name = "book_name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "publish_by", nullable = false)
    private LocalDate publishBy;

    @NotBlank
    @Column(name = "publisher", nullable = false)
    private String publisher;

    @NotNull
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    public Book() {
    }
}
