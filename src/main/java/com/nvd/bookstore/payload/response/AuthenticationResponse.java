package com.nvd.bookstore.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor  // phản hồi xác thực
public class AuthenticationResponse {
    private String token;
}
