package com.ash7nly.monolith.controller;

import com.ash7nly.monolith.dto.request.CreateDriverRequest;
import com.ash7nly.monolith.dto.request.UpdateDriverRequest;
import com.ash7nly.monolith.dto.response.ApiResponse;
import com.ash7nly.monolith.dto.response.DriverResponse;
import com.ash7nly.monolith.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ApiResponse<List<DriverResponse>> getAllDrivers() {
        return ApiResponse.success(driverService.getAllDrivers());
    }

    @GetMapping("/{id}")
    public ApiResponse<DriverResponse> getDriver(@PathVariable Long id) {
        return ApiResponse.success(driverService.getDriver(id));
    }

    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/profile")
    public ApiResponse<DriverResponse> getMyDriverProfile() {
        return ApiResponse.success(driverService.getMyDriverProfile());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users/{userId}")
    public ApiResponse<DriverResponse> createDriverForUser(
            @PathVariable Long userId,
            @Valid @RequestBody CreateDriverRequest request) {
        return ApiResponse.success(driverService.createDriverForUser(userId, request), "Driver profile created successfully");
    }

    @PutMapping("/{id}")
    public ApiResponse<DriverResponse> updateDriver(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDriverRequest request) {
        return ApiResponse.success(driverService.updateDriver(id, request), "Driver profile updated successfully");
    }

    @PutMapping("/{id}/availability")
    public ApiResponse<DriverResponse> updateAvailability(
            @PathVariable Long id,
            @RequestParam boolean available) {
        return ApiResponse.success(driverService.updateAvailability(id, available));
    }
}

