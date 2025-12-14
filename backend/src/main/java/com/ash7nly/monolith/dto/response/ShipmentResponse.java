package com.ash7nly.monolith.dto.response;

import com.ash7nly.monolith.enums.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentResponse {

    private Long id;
    private String trackingNumber;
    private String pickupAddress;
    private String deliveryAddress;
    private String customerName;
    private String customerPhone;
    private String packageWeight;
    private String packageDimension;
    private String packageDescription;
    private Long merchantId;
    private ShipmentStatus status;
    private double cost;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

