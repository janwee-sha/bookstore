package com.janwee.bookstore.book.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "authors")
@Getter
public class AuthorPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1911240306150353773L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_authors")
    @SequenceGenerator(name = "seq_authors", sequenceName = "book.seq_authors", allocationSize = 1)
    private Long id;

    private String name;

    private String profile;

    @Column(name = "phone_no")
    private String phoneNumber;

    public AuthorPO() {
    }

    public AuthorPO(Long id, String name, String profile, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.phoneNumber = phoneNumber;
    }
}
