package com.janwee.bookstore.bookserver.domain;

import java.io.Serializable;

public class Author implements Serializable {
    private Long id;
    private String name;
    private String profile;

    public Author() {
    }

    public Author(Long id, String name, String profile) {
        this.id = id;
        this.name = name;
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfile() {
        return profile;
    }

    public Author withId(Long id) {
        this.id = id;
        return this;
    }

    public Author withName(String name) {
        this.name = name;
        return this;
    }

    public Author addProfile(String profile) {
        this.profile = profile;
        return this;
    }
}
