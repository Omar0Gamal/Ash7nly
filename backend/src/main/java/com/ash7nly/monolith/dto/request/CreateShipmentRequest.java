package com.ash7nly.monolith.dto.request;

import com.ash7nly.monolith.enums.DeliveryArea;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateShipmentRequest {

    @NotBlank(message = "Pickup address is required")
    private String pickupAddress;

    @NotNull(message = "Delivery address is required")
    private DeliveryArea deliveryAddress;

    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    private String customerName;

    @NotBlank(message = "Customer phone is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    private String customerPhone;

    @Email(message = "Invalid email format")
    private String customerEmail;

    @NotBlank(message = "Package weight is required")
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?\\s*kg$", message = "Package weight must be in format like '10kg' or '10.5 kg'")
    private String packageWeight;

    @NotBlank(message = "Package dimension is required")
    private String packageDimension;

    @Size(max = 1000, message = "Package description must not exceed 1000 characters")
    private String packageDescription;

    @PositiveOrZero(message = "Cost must be zero or positive")
    private double cost;
}

