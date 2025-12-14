package com.ash7nly.monolith.mapper;

import com.ash7nly.monolith.dto.request.CreateShipmentRequest;
import com.ash7nly.monolith.dto.response.ShipmentListResponse;
import com.ash7nly.monolith.dto.response.UpdateShipmentStatusResponse;
import com.ash7nly.monolith.entity.Shipment;
import com.ash7nly.monolith.entity.User;
import com.ash7nly.monolith.enums.ShipmentStatus;
import com.ash7nly.monolith.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShipmentMapper {

    private final TrackingMapper trackingMapper;
    private final PricingService pricingService;

    public Shipment toEntity(CreateShipmentRequest request, User merchant) {
        Shipment shipment = new Shipment();
        shipment.setMerchant(merchant);
        shipment.setPickupAddress(request.getPickupAddress());
        shipment.setDeliveryAddress(request.getDeliveryAddress());
        shipment.setCustomerName(request.getCustomerName());
        shipment.setCustomerPhone(request.getCustomerPhone());
        shipment.setCustomerEmail(request.getCustomerEmail());
        shipment.setPackageWeight(request.getPackageWeight());
        shipment.setPackageDimension(request.getPackageDimension());
        shipment.setPackageDescription(request.getPackageDescription());
        shipment.setCost(pricingService.calculateShippingCost(request));
        shipment.setStatus(ShipmentStatus.CREATED);
        shipment.setTrackingNumber(trackingMapper.generateTrackingCode());
        shipment.setActive(true);
        return shipment;
    }

    public ShipmentListResponse toResponse(Shipment entity) {
        ShipmentListResponse response = new ShipmentListResponse();
        response.setMerchantId(entity.getMerchant().getId());
        response.setPickupAddress(entity.getPickupAddress());
        response.setDeliveryAddress(entity.getDeliveryAddress());
        response.setCustomerName(entity.getCustomerName());
        response.setCustomerEmail(entity.getCustomerEmail());
        response.setCost(entity.getCost());
        response.setStatus(entity.getStatus());
        response.setTrackingNumber(entity.getTrackingNumber());
        response.setActive(entity.isActive());
        response.setShipmentId(entity.getShipmentId());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }

    public UpdateShipmentStatusResponse toUpdateResponse(Shipment shipment) {
        UpdateShipmentStatusResponse response = new UpdateShipmentStatusResponse();
        response.setShipmentId(shipment.getShipmentId());
        response.setStatus(shipment.getStatus());
        return response;
    }
}