package com.janwee.bookstore.authorserver.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "tbl_author")
public class Author implements Serializable {
    private static final long serialVersionUID = 1911240306150353773L;
    @Id
    @GeneratedValue(generator = "tbl_author_id_seq")
    private Long id;

    @NotBlank
    @Column(name = "author_name", nullable = false)
    private String name;

    @Column(name = "profile", nullable = false)
    private String profile;

    private String phoneNumber;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
