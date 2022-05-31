package com.janwee.bookstore.authorserver.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "tbl_author")
public class Author implements Serializable {
    @Id
    @GeneratedValue(generator = "tbl_author_id_seq")
    private Long id;

    @NotBlank
    @Column(name = "author_name",nullable = false)
    private String name;

    @Column(name = "profile",nullable = false)
    private String profile;

    public Author() {
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

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
