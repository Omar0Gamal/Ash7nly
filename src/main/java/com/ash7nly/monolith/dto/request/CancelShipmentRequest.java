package com.ash7nly.monolith.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CancelShipmentRequest {

    @NotBlank(message = "Tracking number is required")
    private String trackingNumber;

    private String cancellationReason;

    public CancelShipmentRequest() {
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
}

