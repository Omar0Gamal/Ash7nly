package com.ash7nly.monolith.service;

import com.ash7nly.monolith.dto.request.LoginRequest;
import com.ash7nly.monolith.dto.request.RegisterRequest;
import com.ash7nly.monolith.dto.response.AuthResponse;
import com.ash7nly.monolith.entity.User;
import com.ash7nly.monolith.enums.UserRole;
import com.ash7nly.monolith.exception.BadRequestException;
import com.ash7nly.monolith.exception.UnauthorizedException;
import com.ash7nly.monolith.mapper.UserMapper;
import com.ash7nly.monolith.repository.UserRepository;
import com.ash7nly.monolith.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        if (request.getRole() != null && request.getRole().equals(UserRole.ADMIN)) {
            throw new BadRequestException("Cannot register as ADMIN");
        }

        if (request.getRole() != null && request.getRole().equals(UserRole.DRIVER)) {
            throw new BadRequestException("Cannot register as DRIVER directly. Contact admin.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(request.getRole() != null ? request.getRole() : UserRole.CUSTOMER)
                .isActive(true)
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(user, user.getId(), user.getRole());

        return new AuthResponse(token, UserMapper.toResponse(user));
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        String token = jwtService.generateToken(user, user.getId(), user.getRole());
        return new AuthResponse(token, UserMapper.toResponse(user));
    }
}

