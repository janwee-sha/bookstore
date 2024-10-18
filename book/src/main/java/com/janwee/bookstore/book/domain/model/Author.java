package com.janwee.bookstore.book.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "authors")
@Getter
@Setter
public class Author implements Serializable {
    @Serial
    private static final long serialVersionUID = 1911240306150353773L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private String profile;

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
