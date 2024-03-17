package com.nvd.bookstore.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor  // phản hồi xác thực
public class AuthenticationResponse {
    String name;
    Collection<SimpleGrantedAuthority> roles = new ArrayList<>();
    private String token;
}
