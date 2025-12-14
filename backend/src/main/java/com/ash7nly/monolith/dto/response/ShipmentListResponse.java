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
public class ShipmentListResponse {
    private long shipmentId;
    private String trackingNumber;
    private DeliveryArea deliveryAddress;
    private ShipmentStatus status;
    private String customerEmail;
    private boolean active;
    private LocalDateTime createdAt;
    private long merchantId;
    private String pickupAddress;
    private String customerName;
    private double cost;
}

