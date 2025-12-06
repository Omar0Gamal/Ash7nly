package com.ash7nly.delivery.dto;

public class FailDeliveryRequest {
    private String reason;
    public FailDeliveryRequest() {}
    public FailDeliveryRequest(String reason) { this.reason = reason; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
