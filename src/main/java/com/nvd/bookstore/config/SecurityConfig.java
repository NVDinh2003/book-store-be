package com.nvd.bookstore.config;

import com.nvd.bookstore.config.jwtAuth.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
//    private final LogoutHandler logoutHandler;

    String[] allowURL = {
            "/api/auth/**",
            "/api/products/**",
            "/api/categories/**",
            "/api/publishers/**",
            "/api/authors/**",
            "/api/vouchers/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)   // tắt chức năng bảo vệ CSRF (Cross-Site Request Forgery).
                .authorizeHttpRequests(authorizeRequests ->
                                authorizeRequests
                                        .requestMatchers(allowURL).permitAll()
                                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")

                                        .anyRequest().authenticated()
                )
                // Quản lý session, không tạo session và chỉ sử dụng các session đã tồn tại (nếu có).
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider) // dùng authenticationProvider để xác thực yêu cầu
                // Thêm một bộ lọc JWT trước UsernamePasswordAuthenticationFilter.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .logout((logout) -> logout
//                        .logoutUrl("/api/auth/logout")
//                        .addLogoutHandler(logoutHandler)
//                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
//                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    /*
                              CSRF()
 http.csrf().disable(): để tắt chức năng bảo vệ CSRF (Cross-site Request Forgery) trong ứng dụng web.
 CSRF là một loại tấn công bảo mật mà hacker có thể tạo ra một request trên web bằng cách sử dụng Cookies,
      giả mạo chứng thực request của chủ thể của nó.
 Sử dụng http.csrf().disable() sẽ tắt chức năng bảo vệ CSRF trong ứng dụng.
      Tuy nhiên, điều này chỉ nên được làm khi dịch vụ chỉ được sử dụng bởi các client không sử dụng trình duyệt,
      hoặc nếu nó chỉ được sử dụng bên trong mà không có sự tương tác từ phía người dùng

                          sessionManagement()
 - Các loại SessionCreationPolicy:
        ALWAYS: Luôn tạo một session cho mỗi request.
        ifRequired: Chỉ tạo session nếu cần thiết.
        never: Không bao giờ tạo session.
        stateless: Không tạo session mới hoặc truy cập vào session đã tồn tại. Đây là loại SessionCreationPolicy duy nhất mà Spring Security sử dụng để sử dụng NO SESSIONS.

                          .authenticationProvider(authenticationProvider)
  để cấu hình một trình cung cấp xác thực trong Spring Security.
          Trình cung cấp xác thực là một đối tượng xử lý một yêu cầu xác thực và trả về một đối tượng đã được xác thực đầy đủ.
  Trong Spring Security, mặc định sử dụng ProviderManager làm việc thực tế của xác thực.
          ProviderManager sẽ gọi tới một danh sách các AuthenticationProvider được cấu hình,
          mỗi trình cung cấp được truy vấn theo thứ tự để xem nó có thể thực hiện xác thực không.

          */
}
