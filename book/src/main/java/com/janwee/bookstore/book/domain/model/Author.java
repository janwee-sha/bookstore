package com.janwee.bookstore.book.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "authors")
@Getter
@Setter
public class Author implements Serializable {
    private static final long serialVersionUID = 1911240306150353773L;
    @Id
    @GeneratedValue(generator = "authors_id_seq")
    private Long id;

    @NotBlank
    @Column(name = "author_name", nullable = false)
    private String name;

    @Column(name = "profile", nullable = false)
    private String profile;

    @Column(name = "phone_no")
    private String phoneNumber;

    public Author() {
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String profile() {
        return profile;
    }

    public String phoneNumber() {
        return phoneNumber;
    }
}
