package com.ash7nly.delivery.dto;

import com.ash7nly.common.enums.DeliveryArea;
import com.ash7nly.common.enums.ShipmentStatus;

public class ShipmentListDTO {
    private Long shipmentId;
    private String trackingNumber;
    private DeliveryArea deliveryAdress;
    private String customerName;
    private ShipmentStatus status;
    private Double cost;

    public ShipmentListDTO() {}

    public ShipmentListDTO(
            Long shipmentId,
            String trackingNumber,
            DeliveryArea deliveryAdress,
            String customerName,
            ShipmentStatus status,
            Double cost
    ) {
        this.shipmentId = shipmentId;
        this.trackingNumber = trackingNumber;
        this.deliveryAdress = deliveryAdress;
        this.customerName = customerName;
        this.status = status;
        this.cost = cost;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public DeliveryArea getDeliveryAdress() {
        return deliveryAdress;
    }

    public void setDeliveryAdress(DeliveryArea deliveryAdress) {
        this.deliveryAdress = deliveryAdress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}
