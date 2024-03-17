package com.nvd.bookstore.config.jwtAuth;

import com.nvd.bookstore.config.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override     // chỉ hoạt động mỗi khi nhận được yêu cầu
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

//      header "Authorization" chứa chuỗi JWT dùng để xác thực người dùng và cung cấp thông tin về người dùng cho app.
        final String authHeader = request.getHeader(HEADER_STRING);
        final String jwt;       //  chuỗi mã hóa được gửi kèm trong Header
        final String userEmail;


//      check header của request chứa thông tin token JWT không,
//      có thì xác minh token này, không thì yêu cầu sẽ được đi tiếp mà không cần xử lý.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7); // 'Bearer ' chứa 7 ký tự, lấy phần jwt phía sau
        userEmail = jwtService.extractUsername(jwt);    // extract the userEmail from JWT token;

//      xác thực userEmail lấy từ JWT token
        /*
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                      userDetails lấy từ DataBase
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

//          Check token có hợp lệ với người dùng không
            if (jwtService.isTokenValid(jwt, userDetails)) {
                List<String> roles = jwtService.extractClaim(jwt, (Claims claims) -> claims.get("roles", List.class));
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
//      UsernamePasswordAuthenticationToken: chứa thông tin login (username, pass, quyền,..) của user
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,    // chi tiết user
                        null,       // credentials: thông tin đăng nhập, null vì đã xác thực  = JWT token
                        userDetails.getAuthorities()    // danh sách roles
                );

//          gán chi tiết xác thực từ 'request' được tạo từ WebAuthenticationDetails
//          chứa thông tin chi tiết về yêu cầu web, như địa chỉ IP hoặc thông tin đặc tả khác.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//          set thông tin xác thực user vào contextHolder().
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }   // END-check token
        } // END-xác thực email từ jwt

         */

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                List<SimpleGrantedAuthority> grantedAuthorities = userDetails.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        grantedAuthorities
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

//      thực hiện filter với request và respon hiện tại, nếu tất cả Filter được xử lý xong, yêu cầu sẽ được chuyển đến controller để xử lý
        filterChain.doFilter(request, response);
    }

 /*   @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;}

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    } */

}