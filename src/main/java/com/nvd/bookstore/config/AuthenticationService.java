package com.nvd.bookstore.config;


import com.nvd.bookstore.config.jwtAuth.JwtService;
import com.nvd.bookstore.entity.Role;
import com.nvd.bookstore.entity.User;
import com.nvd.bookstore.payload.request.LoginRequest;
import com.nvd.bookstore.payload.request.RegisterRequest;
import com.nvd.bookstore.payload.response.AuthenticationResponse;
import com.nvd.bookstore.repository.RoleRepository;
import com.nvd.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;  // xử lý việc xác thực

    public AuthenticationResponse register(RegisterRequest request) {
//        Logger logger = LoggerFactory.getLogger(AuthenticationResponse.class);

        if (repository.existsByEmail(request.getEmail()) || repository.existsByPhone(request.getPhone())) {
            throw new UsernameNotFoundException("User with email or phone already exists");
        }

        // Khởi tạo danh sách roles nếu chưa tồn tại
        List<Role> roles = new ArrayList<>();

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .roles(roles)
                .status(true)
                .build();

        // Assign roles to the user
        Role role = roleRepository.findByName("USER"); // Assuming you have a role named ROLE_USER
        if (role == null) {
            throw new RuntimeException("Default role not found");
        }
        user.getRoles().add(role);

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
//      được dùng trong việc xử lý authentication, chứa usernaem, pass. Sau khi qua AuthenticationFilter
//      thông tin đăng nhập sẽ được convert sang 'Authentication' với UsernamePasswordAuthenticationToken là một implementation của nó.
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Nếu xác thực thành công, lấy thông tin người dùng từ cơ sở dữ liệu
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = repository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // In ra các vai trò của người dùng (đảm bảo rằng việc lấy thông tin người dùng và vai trò hoạt động đúng)
        System.out.println("User roles: " + user.getRoles().get(1).getName());


        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}

