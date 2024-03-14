package com.nvd.bookstore.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PublisherRequest {
    private String name;
    @JsonProperty("isDeleted")
    private boolean isDeleted;
}
