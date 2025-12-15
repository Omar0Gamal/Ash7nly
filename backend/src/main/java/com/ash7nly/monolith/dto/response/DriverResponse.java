package com.ash7nly.monolith.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponse {
    private Long id;
    private Long userId;
    private String username;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String vehicleType;
    private String vehicleNumber;
    private String licenseNumber;
    private String serviceArea;
    private boolean isAvailable;
}