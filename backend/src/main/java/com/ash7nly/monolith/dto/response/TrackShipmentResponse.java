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
public class TrackShipmentResponse {

    private String trackingNumber;
    private String pickupAddress;
    private DeliveryArea deliveryAddress;
    private Long merchantId;
    private ShipmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

