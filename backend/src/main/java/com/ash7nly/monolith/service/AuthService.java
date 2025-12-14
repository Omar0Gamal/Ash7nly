package com.ash7nly.monolith.service;

import com.ash7nly.monolith.dto.request.RegisterDriverRequest;
import com.ash7nly.monolith.dto.request.LoginRequest;
import com.ash7nly.monolith.dto.request.RegisterMerchantRequest;
import com.ash7nly.monolith.dto.response.AuthResponse;
import com.ash7nly.monolith.dto.response.DriverResponse;
import com.ash7nly.monolith.dto.response.UserResponse;
import com.ash7nly.monolith.entity.Driver;
import com.ash7nly.monolith.entity.User;
import com.ash7nly.monolith.enums.DeliveryArea;
import com.ash7nly.monolith.enums.UserRole;
import com.ash7nly.monolith.exception.BadRequestException;
import com.ash7nly.monolith.exception.UnauthorizedException;
import com.ash7nly.monolith.mapper.DriverMapper;
import com.ash7nly.monolith.mapper.UserMapper;
import com.ash7nly.monolith.repository.DriverRepository;
import com.ash7nly.monolith.repository.UserRepository;
import com.ash7nly.monolith.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse<UserResponse> registerMerchant(RegisterMerchantRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }
        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(UserRole.MERCHANT)
                .isActive(true)
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(user, user.getId(), user.getRole());

        log.info("User registered successfully: {}", user.getEmail());
        return new AuthResponse<>(token, UserMapper.toResponse(user));
    }

    public AuthResponse<DriverResponse> registerDriver(RegisterDriverRequest request)
    {
        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(UserRole.DRIVER)
                .isActive(true)
                .build();

        user = userRepository.save(user);

        Driver driver = Driver.builder()
                .user(user)
                .vehicleType(request.getVehicleType())
                .vehicleNumber(request.getVehicleNumber())
                .licenseNumber(request.getLicenseNumber())
                .serviceArea(DeliveryArea.valueOf(request.getServiceArea()))
                .isAvailable(true)
                .build();

        driver = driverRepository.save(driver);
        String token = jwtService.generateToken(user, user.getId(), user.getRole());
        log.info("Driver registered successfully: {}", user.getEmail());

        return new AuthResponse<>(token, driverMapper.buildDriverResponse(driver, user));
    }

    public AuthResponse<UserResponse> login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            log.warn("Failed login attempt for email: {}", request.getEmail());
            throw new UnauthorizedException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        String token = jwtService.generateToken(user, user.getId(), user.getRole());

        log.info("User logged in successfully: {}", user.getEmail());
        return new AuthResponse<>(token, UserMapper.toResponse(user));
    }
}
