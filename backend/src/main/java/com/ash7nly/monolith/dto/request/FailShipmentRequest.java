package com.ash7nly.monolith.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FailShipmentRequest {


    @NotBlank(message = "Cancellation reason is required")
    @Size(min = 5, max = 500, message = "Cancellation reason must be between 5 and 500 characters")
    private String cancellationReason;
}

