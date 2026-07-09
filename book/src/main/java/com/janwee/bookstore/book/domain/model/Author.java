package com.janwee.bookstore.book.domain.model;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;

@Builder
public class Author implements Serializable {
    @Serial
    private static final long serialVersionUID = 1911240306150353773L;

    private Long id;

    private String name;

    private String profile;

    private String phoneNumber;

    public Author() {
    }

    public Author(Long id, String name, String profile, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.phoneNumber = phoneNumber;
    }

    public Long id() {
        return id;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void renameTo(String name) {
        this.name = name;
    }

    public String profile() {
        return profile;
    }

    public void changeProfileTo(String profile) {
        this.profile = profile;
    }

    public String phoneNumber() {
        return phoneNumber;
    }

    public void changePhoneNumberTo(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
