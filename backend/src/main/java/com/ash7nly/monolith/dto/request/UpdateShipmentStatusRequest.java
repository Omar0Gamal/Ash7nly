package com.ash7nly.monolith.dto.request;

import com.ash7nly.monolith.enums.ShipmentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShipmentStatusRequest {

    @NotNull(message = "Shipment ID is required")
    @Positive(message = "Shipment ID must be positive")
    private Long shipmentId;

    @NotNull(message = "Status is required")
    private ShipmentStatus status;
}

