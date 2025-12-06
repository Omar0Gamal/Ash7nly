package com.ash7nly.delivery. dto;

import java.time.LocalDateTime;

public class DeliveryResponse {

    private Long id;
    private Long shipmentId;
    private Long driverId;
    private LocalDateTime assignedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime pickedUpAt;
    private String recipientName;
    private String deliveryNotes;
    public DeliveryResponse() {}
    public DeliveryResponse(Long id, Long shipmentId, Long driverId, LocalDateTime assignedAt,
                            LocalDateTime acceptedAt, LocalDateTime deliveredAt, LocalDateTime pickedUpAt,
                            String recipientName, String deliveryNotes) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.driverId = driverId;
        this.assignedAt = assignedAt;
        this.acceptedAt = acceptedAt;
        this.deliveredAt = deliveredAt;
        this.pickedUpAt = pickedUpAt;
        this.recipientName = recipientName;
        this.deliveryNotes = deliveryNotes;
    }
    // getters and setters
    public Long getId() { return id; }
    public Long getShipmentId() { return shipmentId; }
    public Long getDriverId() { return driverId; }
    public java.time.LocalDateTime getAssignedAt() { return assignedAt; }
    public java.time.LocalDateTime getAcceptedAt() { return acceptedAt; }
    public java.time.LocalDateTime getDeliveredAt() { return deliveredAt; }
    public java.time.LocalDateTime getPickedUpAt() { return pickedUpAt; }
    public String getRecipientName() { return recipientName; }
    public String getDeliveryNotes() { return deliveryNotes; }

    public void setId(Long id) { this.id = id; }
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
    public void setAcceptedAt(LocalDateTime acceptedAt) { this.acceptedAt = acceptedAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
    public void setPickedUpAt(LocalDateTime pickedUpAt) { this.pickedUpAt = pickedUpAt; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public void setDeliveryNotes(String deliveryNotes) { this.deliveryNotes = deliveryNotes; }
}