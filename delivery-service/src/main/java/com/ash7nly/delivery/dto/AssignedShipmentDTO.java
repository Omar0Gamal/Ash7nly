package com.ash7nly.delivery.dto;

import com.ash7nly.common.enums.DeliveryArea;

public class AssignedShipmentDTO {
    private Long shipmentId;
    private String trackingNumber;
    private DeliveryArea deliveryAddress;
    private String customerName;

    public AssignedShipmentDTO() {}

    public AssignedShipmentDTO(Long shipmentId, String trackingNumber, DeliveryArea deliveryAddress, String customerName) {
        this.shipmentId = shipmentId;
        this.trackingNumber = trackingNumber;
        this.deliveryAddress = deliveryAddress;
        this.customerName = customerName;
    }

    public Long getShipmentId() { return shipmentId; }
    public String getTrackingNumber() { return trackingNumber; }
    public DeliveryArea getDeliveryAddress() { return deliveryAddress; }
    public String getCustomerName() { return customerName; }

    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public void setDeliveryAddress(DeliveryArea deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}
