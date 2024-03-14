package com.nvd.bookstore.config.jwtAuth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service        // extract claim: trích xuất nội dung của thông tin (claim).
public class JwtService {
    //    Tạo từ https://www.allkeysgenerator.com/ ( Encrytion Key - 256 byte - Hex)
    private static final String SECRET_KEY = "2948404D635166546A576E5A7134743777217A25432A2D4A614E645267556B58";
    //Thời gian có hiệu lực của chuỗi jwt
    private final long JWT_EXPIRATION = 1000 * 60 * 24;

//  ********************************************** Tạo token

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    //   Map: K-V - extraClaims có thể chứa infors như roles, expiryDate token, vv. Giúp cho xác thực và xử lý token trở nên dễ dàng hơn.
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()   // tạo builder xây dựng 1 token JWT mới.
                .setClaims(extraClaims)    // thêm các trường bổ sung cho token
                .setSubject(userDetails.getUsername())  // add username vào token
                .setIssuedAt(new Date(System.currentTimeMillis()))      // time tạo token
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)     // signature của token
                .compact(); // trả về JWT token dưới dạng một compact string-một chuỗi mã hóa base64 và các thành phần JWT token ngăn cách bởi dấu .(dot).
    }


//  *********************************************** Check token


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // so time hết hạn với thời gian hiện tại (Date).
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);  // lấy thời gian hết hạn từ Claims, trả về dưới dạng Date.
    }

//  *********************************************** extract JWT token

    // Get Username từ jwt token
    public String extractUsername(String token) {   // trả về subject của JWT, tức là username của User. (kiểu T - String)
        return extractClaim(token, Claims::getSubject); // Claims::getSubject: chuyển đổi từ Claims sang String
    }

    //  extract 1 thông tin (claim) từ token JWT.
    // Function<Claims, T> claimsResolver: chuyển đổi xử lý dữ liệu trích xuất từ token JWT (Claims) sang một đầu ra có kiểu dữ liệu T.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);    // apply chuyển đổi DL extract từ claims trả về kiểu T
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()    //  tạo đối tượng JwtParser
                .setSigningKey(getSignInKey())  // set SigningKey để giải mã JWT.
                .build()    // tạo đối tượng JwtParser đã được cấu hình.
                .parseClaimsJws(token)      // giải mã JWT và trả về một đối tượng Jws.
                .getBody();     // trả về 1 đối tượng Claims chứa các Claims (các thông tin được chứa trong JWT).
    }

    //  Tạo 1 "khoá đăng nhập" - (Sign In Key)
    private Key getSignInKey() {
        // Decoders.BASE64.decode(SECRET_KEY) dùng thư viện Base64 giải mã một chuỗi mã hóa dạng Base64 từ biến SECRET_KEY. Trả về mảng byte.
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        // Keys.hmacShaKeyFor(keyBytes) dùng thư viện Keys tạo một khóa HMAC (Hash-based Message Authentication Code)
        // bằng thuật toán SHA (Secure Hash Algorithm). Đầu vào là mảng byte trên.
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

