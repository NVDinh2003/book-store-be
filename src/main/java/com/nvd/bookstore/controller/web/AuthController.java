package com.nvd.bookstore.controller.web;

import com.nvd.bookstore.config.AuthenticationService;
import com.nvd.bookstore.payload.request.LoginRequest;
import com.nvd.bookstore.payload.request.RegisterRequest;
import com.nvd.bookstore.payload.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Test Auth");
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
}
