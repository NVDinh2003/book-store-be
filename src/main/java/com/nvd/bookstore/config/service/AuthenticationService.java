package com.nvd.bookstore.config.service;


import com.nvd.bookstore.entity.Provider;
import com.nvd.bookstore.entity.Role;
import com.nvd.bookstore.entity.User;
import com.nvd.bookstore.payload.request.AuthenticationRequest;
import com.nvd.bookstore.payload.request.RegisterRequest;
import com.nvd.bookstore.payload.response.AuthenticationResponse;
import com.nvd.bookstore.repository.RoleRepository;
import com.nvd.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;  // xử lý việc xác thực

    public AuthenticationResponse register(RegisterRequest request) {
//        Logger logger = LoggerFactory.getLogger(AuthenticationResponse.class);

        if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByPhone(request.getPhone())) {
            throw new UsernameNotFoundException("User with email or phone already exists");
        }

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .providerId(Provider.local.name())
                .status(true)
                .build();


        Role role = roleRepository.findByName("ROLE_USER");

        // Kiểm tra xem vai trò có tồn tại hay không
        if (role == null) {
            throw new RuntimeException("Default role not found");
        }

        // Thêm vai trò cho người dùng
        user.getRoles().add(role);

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
////      được dùng trong việc xử lý authentication, chứa usernaem, pass. Sau khi qua AuthenticationFilter
////      thông tin đăng nhập sẽ được convert sang 'Authentication' với UsernamePasswordAuthenticationToken là một implementation của nó.
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getPassword());

            // Thực hiện xác thực thông tin đăng nhập
            Authentication authentication = authenticationManager.authenticate(token);

            authenticationManager.authenticate(token);

            // Nếu xác thực thành công, lấy thông tin người dùng từ cơ sở dữ liệu
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> user = userRepository.findByEmail(userDetails.getUsername());

            if (user.isPresent()) {
                // Lấy danh sách quyền của người dùng từ cơ sở dữ liệu
                List<SimpleGrantedAuthority> authorities = user.get().getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList());

                var jwtToken = jwtService.generateToken(user.get());
                return AuthenticationResponse.builder()
                        .token(jwtToken)
                        .name(user.get().getFullName())
                        .roles(authorities)
                        .build();
            } else throw new UsernameNotFoundException("User not found");

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
