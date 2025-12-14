package com.ash7nly.monolith.dto.request;

import com.ash7nly.monolith.enums.ShipmentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDeliveryStatusRequest {


    @NotNull(message = "Status is required")
    private ShipmentStatus status;

    @Size(max = 500, message = "Delivery notes must not exceed 500 characters")
    private String deliveryNotes;
}