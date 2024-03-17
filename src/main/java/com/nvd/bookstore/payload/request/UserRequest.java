package com.nvd.bookstore.payload.request;

import com.nvd.bookstore.entity.Role;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class UserRequest {
    private String address;
    private String avatar;
    private String email;
    private String fullName;
    private String password;
    private String phone;
    private Boolean status;
    private List<Role> roles = new ArrayList<>();

    public UserRequest(String address, String avatar, String email,
                       String fullName, String password, String phone) {
        this.address = address;
        this.avatar = avatar;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.phone = phone;
        this.status = true;
    }
}
