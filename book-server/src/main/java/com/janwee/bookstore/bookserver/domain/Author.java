package com.janwee.bookstore.bookserver.domain;

import java.io.Serializable;

public class Author implements Serializable {
    private final Long id;
    private final String name;
    private final String profile;

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
}
