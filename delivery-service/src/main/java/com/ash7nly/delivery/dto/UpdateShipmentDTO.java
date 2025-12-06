package com.ash7nly.delivery.dto;

import com.ash7nly.common.enums.ShipmentStatus;

public class UpdateShipmentDTO {
    private Long shipmentID;
    private ShipmentStatus status;

    public UpdateShipmentDTO() {}

    public UpdateShipmentDTO(Long shipmentID, ShipmentStatus status) {
        this.shipmentID = shipmentID;
        this.status = status;
    }

    public Long getShipmentID() {
        return shipmentID;
    }

    public void setShipmentID(Long shipmentID) {
        this.shipmentID = shipmentID;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }
}
