package com.janwee.bookstore.book.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
