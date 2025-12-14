package com.ash7nly.monolith.controller;

import com.ash7nly.monolith.aspect.RateLimit;
import com.ash7nly.monolith.dto.request.RegisterDriverRequest;
import com.ash7nly.monolith.dto.request.LoginRequest;
import com.ash7nly.monolith.dto.request.RegisterMerchantRequest;
import com.ash7nly.monolith.dto.response.ApiResponse;
import com.ash7nly.monolith.dto.response.AuthResponse;
import com.ash7nly.monolith.dto.response.DriverResponse;
import com.ash7nly.monolith.dto.response.UserResponse;
import com.ash7nly.monolith.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @RateLimit(requests = 5, seconds = 60)
    public ApiResponse<AuthResponse<UserResponse>> registerMerchant(@Valid @RequestBody RegisterMerchantRequest request) {
        return ApiResponse.success(authService.registerMerchant(request), "Merchant registered successfully");
    }

    @PostMapping("/login")
    @RateLimit(requests = 10, seconds = 60)
    public ApiResponse<AuthResponse<UserResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request), "Login successful");
    }

    @PostMapping("/register/driver")
    @RateLimit(requests = 5, seconds = 60)
    public ApiResponse<AuthResponse<DriverResponse>> registerDriver(@Valid @RequestBody RegisterDriverRequest request) {
        return ApiResponse.success(
                authService.registerDriver(request),
                "Driver registered successfully"
        );
    }
}
