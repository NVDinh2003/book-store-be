package com.nvd.bookstore.payload.request;

import lombok.Data;

@Data
public class UserStatusRequest {

    private Long userId;
    private Boolean status;
}
