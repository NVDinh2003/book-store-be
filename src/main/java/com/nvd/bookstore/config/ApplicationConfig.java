package com.nvd.bookstore.config;

import com.nvd.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {


    private final UserRepository repository;

    @Bean       // Khi run, ApplicationConfig sẽ được tự động đăng ký với context của ứng dụng và userDetailsService()
    public UserDetailsService userDetailsService() {
        // method ở JWTAuthenticationFilter
        return username -> repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }


    @Bean       // quản lý xác thực người dùng.
    public AuthenticationProvider authenticationProvider() {
//     DaoAuthenticationProvider là một thành phần của Spring Security
//     được sử dụng để xác thực tên người dùng và mật khẩu sử dụng UserDetailsService và PasswordEncoder
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean   // tạo ra một AuthenticationManager, một chức năng bảo mật để xác thực người dùng.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
