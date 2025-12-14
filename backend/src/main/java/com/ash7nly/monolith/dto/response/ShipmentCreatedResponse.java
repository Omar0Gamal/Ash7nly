package com.ash7nly.monolith.dto.response;

import com.ash7nly.monolith.enums.DeliveryArea;
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
public class ShipmentCreatedResponse {
    private Long shipmentId;
    private String trackingNumber;
    private String pickupAddress;
    private DeliveryArea deliveryAddress;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String packageWeight;
    private String packageDimension;
    private String packageDescription;
    private Long merchantId;
    private ShipmentStatus status;
    private double cost;
    private boolean isActive;
    private LocalDateTime createdAt;
    private Long paymentId;
    private String paymentMessage;
}

