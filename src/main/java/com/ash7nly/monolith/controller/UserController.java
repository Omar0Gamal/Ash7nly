package com.ash7nly.monolith.controller;

import com.ash7nly.monolith.dto.request.UserUpdateRequest;
import com.ash7nly.monolith.dto.response.ApiResponse;
import com.ash7nly.monolith.dto.response.UserResponse;
import com.ash7nly.monolith.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ApiResponse<UserResponse> getMyProfile() {
        return ApiResponse.success(userService.getMyProfile());
    }

    @PutMapping("/profile")
    public ApiResponse<UserResponse> updateMyProfile(@RequestBody UserUpdateRequest request) {
        return ApiResponse.success(userService.updateUser(userService.getMyProfile().getId(), request));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        return ApiResponse.success(userService.getUserById(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        return ApiResponse.success(userService.updateUser(id, request));
    }
}

