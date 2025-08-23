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

    public static class Builder {
        private final Author author;

        public Builder() {
            this.author = new Author();
        }

        public Builder ofName(String name) {
            this.author.name = name;
            return this;
        }

        public Builder withProfile(String profile) {
            this.author.profile = profile;
            return this;
        }

        public Builder withPhoneNumber(String phoneNumber) {
            this.author.phoneNumber = phoneNumber;
            return this;
        }

        public Author build() {
            return this.author;
        }
    }
}
