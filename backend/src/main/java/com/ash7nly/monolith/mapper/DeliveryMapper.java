package com.ash7nly.monolith.mapper;

import com.ash7nly.monolith.dto.response.AvailableDeliveryResponse;
import com.ash7nly.monolith.dto.response.DeliveryResponse;
import com.ash7nly.monolith.entity.Delivery;
import com.ash7nly.monolith.entity.Shipment;
import org.springframework.stereotype.Component;

@Component
public class DeliveryMapper {

    public DeliveryResponse toResponse(Delivery delivery) {
        if (delivery == null) {
            return null;
        }

        DeliveryResponse response = new DeliveryResponse();
        response.setId(delivery.getId());

        if (delivery.getShipment() != null) {
            response.setShipmentId(delivery.getShipment().getShipmentId());
            response.setTrackingNumber(delivery.getShipment().getTrackingNumber());
            response.setShipmentStatus(delivery.getShipment().getStatus().toString());
        }

        if (delivery.getDriver() != null) {
            response.setDriverId(delivery.getDriver().getId());
            if (delivery.getDriver().getUser() != null) {
                response.setDriverName(delivery.getDriver().getUser().getFullName());
            }
        }

        response.setAssignedAt(delivery.getAssignedAt());
        response.setAcceptedAt(delivery.getAcceptedAt());
        response.setPickedUpAt(delivery.getPickedUpAt());
        response.setDeliveredAt(delivery.getDeliveredAt());
        response.setRecipientName(delivery.getRecipientName());
        response.setDeliveryNotes(delivery.getDeliveryNotes());

        return response;
    }

    public AvailableDeliveryResponse toAvailableDeliveryResponse(Delivery delivery) {
        if (delivery == null || delivery.getShipment() == null) {
            return null;
        }

        Shipment shipment = delivery.getShipment();

        return AvailableDeliveryResponse.builder()
                .deliveryId(delivery.getId())
                .shipmentId(shipment.getShipmentId())
                .trackingNumber(shipment.getTrackingNumber())
                .deliveryAddress(shipment.getDeliveryAddress())
                .status(shipment.getStatus())
                .customerEmail(shipment.getCustomerEmail())
                .customerName(shipment.getCustomerName())
                .pickupAddress(shipment.getPickupAddress())
                .cost(shipment.getCost())
                .createdAt(shipment.getCreatedAt())
                .build();
    }
}
