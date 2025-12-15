package com.ash7nly.monolith.controller;

import com.ash7nly.monolith.dto.request.*;
import com.ash7nly.monolith.dto.response.*;
import com.ash7nly.monolith.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<List<DriverResponse>> getAllDrivers() {
        return ApiResponse.success(driverService.getAllDrivers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<DriverResponse> getDriver(@PathVariable Long id) {
        return ApiResponse.success(driverService.getDriver(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<DriverResponse> updateDriver(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDriverRequest request) {
        return ApiResponse.success(driverService.updateDriver(id, request), "Driver profile updated successfully");
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('DRIVER')")
    public ApiResponse<DriverResponse> getMyDriverProfile() {
        return ApiResponse.success(driverService.getMyDriverProfile());
    }

    @PutMapping("/me")
    @PreAuthorize("hasAuthority('DRIVER')")
    public ApiResponse<DriverResponse> updateMyDriverProfile(@Valid @RequestBody UpdateDriverRequest request) {
        DriverResponse currentDriver = driverService.getMyDriverProfile();
        return ApiResponse.success(driverService.updateDriver(currentDriver.getId(), request), "Driver profile updated successfully");
    }

    @PreAuthorize("hasAuthority('DRIVER')")
    @PutMapping("/me/availability")
    public ApiResponse<DriverResponse> updateAvailability(@Valid @RequestBody UpdateDriverAvailabilityRequest request) {
        DriverResponse currentDriver = driverService.getMyDriverProfile();
        return ApiResponse.success(driverService.updateAvailability(currentDriver.getId(), request.getAvailable()));
    }
}
