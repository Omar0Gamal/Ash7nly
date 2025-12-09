package com.ash7nly.monolith.dto.response;

public class CancelShipmentResponse {
    private String trackingNumber;
    private String status;
    private String message;

    public CancelShipmentResponse() {
    }

    public CancelShipmentResponse(String trackingNumber, String status, String message) {
        this.trackingNumber = trackingNumber;
        this.status = status;
        this.message = message;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

