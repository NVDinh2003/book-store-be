package com.nvd.bookstore.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class AuthorRequest {

    private String name;
    private String description;
    @JsonProperty("isDeleted")
    private boolean isDeleted;

    public AuthorRequest(String name, String description) {
        this.name = name;
        this.description = description;
        this.isDeleted = false;
    }
}
