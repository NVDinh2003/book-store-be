package com.nvd.bookstore.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    @JsonProperty("isActive")
    private boolean isActive;
    @JsonProperty("isDeleted")
    private boolean isDeleted;

    public CategoryRequest(String name) {
        this.name = name;
        this.isActive = true;
        this.isDeleted = false;
    }
}
