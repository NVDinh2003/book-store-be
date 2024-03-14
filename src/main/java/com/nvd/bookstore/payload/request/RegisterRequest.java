package com.nvd.bookstore.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterRequest {
    @CreationTimestamp
    private Date createdAt;

    private String email;
    private String fullName;
    private String password;
    private String phone;
}
