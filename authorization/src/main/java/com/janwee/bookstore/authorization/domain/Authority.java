package com.janwee.bookstore.authorization.domain;

public enum Authority {
    USER_READ("user:read"),
    USER_WRITE("user:write");

    private final String value;

    Authority(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
