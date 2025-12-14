package com.ash7nly.monolith.mapper;

import com.ash7nly.monolith.dto.response.TrackShipmentResponse;
import com.ash7nly.monolith.dto.response.TrackingHistoryResponse;
import com.ash7nly.monolith.entity.Shipment;
import com.ash7nly.monolith.entity.TrackingHistory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TrackingMapper {

    private static final String TRACKING_PREFIX = "TRK";
    private static final int TRACKING_CODE_LENGTH = 12;

    public TrackShipmentResponse toResponse(Shipment entity) {
        TrackShipmentResponse response = new TrackShipmentResponse();
        response.setPickupAddress(entity.getPickupAddress());
        response.setDeliveryAddress(entity.getDeliveryAddress());
        response.setMerchantId(entity.getMerchant().getId());
        response.setStatus(entity.getStatus());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setTrackingNumber(entity.getTrackingNumber());
        return response;
    }

    public TrackingHistoryResponse toResponse(TrackingHistory history) {
        TrackingHistoryResponse response = new TrackingHistoryResponse();
        response.setStatus(history.getShipmentStatus());
        response.setTimestamp(history.getTimestamp());
        return response;
    }

    public String generateTrackingCode() {
        String uuid = UUID.randomUUID().toString();
        String uniqueBase = uuid.replace("-", "").toUpperCase();
        String uniqueCode = uniqueBase.substring(0, TRACKING_CODE_LENGTH);
        return TRACKING_PREFIX + uniqueCode;
    }
}