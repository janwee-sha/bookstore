package com.janwee.bookstore.gatewayserver.domain;

public enum FilterType {
    PRE("pre"),
    POST("post"),
    ROUTE("route");
    private final String code;

    FilterType(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
