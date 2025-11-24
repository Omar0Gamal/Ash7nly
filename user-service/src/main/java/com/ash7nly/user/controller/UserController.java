package com.ash7nly.user.controller;

import com.ash7nly.common.dto.UserDto;
import com.ash7nly.common.response.ApiResponse;
import com.ash7nly.common.security.UserContext;
import com.ash7nly.user.dto.req.UserUpdateRequest;
import com.ash7nly.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ApiResponse<UserDto> getMyProfile() {
        Long userId = Long.valueOf(UserContext.getUserId());
        return ApiResponse.success(userService.getUserById(userId));
    }

    @PutMapping("/profile")
    public ApiResponse<UserDto> updateProfile(@RequestBody UserUpdateRequest request) {
        Long userId = Long.valueOf(UserContext.getUserId());
        return ApiResponse.success(userService.updateUser(userId, request));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDto> getUserById(@PathVariable Long id) {
        return ApiResponse.success(userService.getUserById(id));
    }
}
