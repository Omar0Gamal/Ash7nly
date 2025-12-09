package com.ash7nly.monolith.dto.response;

import com.ash7nly.monolith.enums.VehicleType;

import java.time.LocalDateTime;

public class DriverResponse {
    private Long id;
    private Long userId;
    private VehicleType vehicleType;
    private String vehicleNumber;
    private String licenseNumber;
    private String serviceArea;
    private Boolean isAvailable;
    private Integer deliveriesCount;
    private LocalDateTime createdAt;

    public DriverResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Integer getDeliveriesCount() {
        return deliveriesCount;
    }

    public void setDeliveriesCount(Integer deliveriesCount) {
        this.deliveriesCount = deliveriesCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

