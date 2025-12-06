package com.ash7nly.delivery. Services;

import com.ash7nly.common.enums.ShipmentStatus;
import com.ash7nly.common.response.ApiResponse;
import com.ash7nly.delivery.Entity.Delivery;
import com.ash7nly.delivery.Entity.Driver;
import com.ash7nly.delivery.client.ShipmentClient;
import com.ash7nly.delivery.dto.*;
import com.ash7nly.delivery. repository.DeliveryRepository;
import com.ash7nly.delivery.repository.DriverRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org. springframework.stereotype.Service;

import java.time. LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DriverRepository driverRepository;
    private final ShipmentClient shipmentClient;

    public DeliveryService(DeliveryRepository deliveryRepository, DriverRepository driverRepository, ShipmentClient shipmentClient) {
        this.deliveryRepository = deliveryRepository;
        this.driverRepository = driverRepository;
        this.shipmentClient = shipmentClient;
    }
    @Transactional
    public DeliveryResponse createDeliveryFromShipment(CreateDeliveryRequest request) {
        Delivery d = new Delivery();
        d.setShipmentId(request.getShipmentId());
        d.setRecipientName(request.getRecipientName());
        // driver null initially
        Delivery saved = deliveryRepository.save(d);
        return toResponse(saved);
    }
    public List<AssignedShipmentDTO> getAvailableShipmentsForDriver(Long driverId) {
        // 1) find driver by userId
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found for userId: " + driverId));

        // 2) call shipment service to get shipments by area
        ResponseEntity<ApiResponse<List<ShipmentListDTO>>> resp =
                shipmentClient.getByServiceArea(driver.getServiceArea());

        if (resp == null || resp.getBody() == null || resp.getBody().getData() == null) {
            return new ArrayList<>();
        }

        List<ShipmentListDTO> shipments = resp.getBody().getData();
        List<AssignedShipmentDTO> result = new ArrayList<>();
        for (ShipmentListDTO s : shipments) {
            AssignedShipmentDTO dto = new AssignedShipmentDTO();
            dto.setShipmentId(s.getShipmentId());
            dto.setTrackingNumber(s.getTrackingNumber());
            dto.setDeliveryAddress(s.getDeliveryAdress());
            dto.setCustomerName(s.getCustomerName());
            result.add(dto);
        }

        return result;
    }
    @Transactional
    public DeliveryResponse acceptShipment(Long shipmentId, Long driverId) {

        // 1) find driver entity by userId
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        // 2) check or create delivery record
        Optional<Delivery> existing = deliveryRepository.findByShipmentId(shipmentId);
        Delivery d;
        if (existing.isPresent()) {
            d = existing.get();
        } else {
            d = new Delivery();
            d.setShipmentId(shipmentId);
        }

        // 3) assign driver
        d.setDriver(driver);
        LocalDateTime now = LocalDateTime.now();
        d.setAssignedAt(now);
        d.setAcceptedAt(now);

        Delivery saved = deliveryRepository.save(d);

        // 4) notify shipment service to set status ASSIGNED
        UpdateShipmentDTO upd = new UpdateShipmentDTO();
        upd.setShipmentID(shipmentId);
        upd.setStatus(ShipmentStatus.ASSIGNED);
        try {
            shipmentClient.updateStatus(upd);
        } catch (Exception e) {
            // log and continue; consider compensating action on failure
            System.err.println("Failed to update shipment status: " + e.getMessage());
        }

        return toResponse(saved);
    }
    public List<DeliveryResponse> getAssignedDeliveriesForDriver(Long userId) {

        Driver driver = driverRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        List<Delivery> deliveries = deliveryRepository.findByDriverId(driver.getId());

        List<DeliveryResponse> list = new ArrayList<>();
        for (Delivery d : deliveries) {
            list.add(toResponse(d));
        }
        return list;
    }

    @Transactional
    public DeliveryResponse updateDeliveryStatus(Long deliveryId, String status) {
        Delivery d = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        // call shipment service to update shipment status (source of truth)
        UpdateShipmentDTO upd = new UpdateShipmentDTO();
        upd.setShipmentID(d.getShipmentId());
        try {
            upd.setStatus(ShipmentStatus.valueOf(status));
        } catch (Exception ex) {
            throw new RuntimeException("Invalid shipment status: " + status);
        }
        shipmentClient.updateStatus(upd);

        // set local timestamps when relevant:
        if ("PICKED_UP".equalsIgnoreCase(status)) {
            d.setPickedUpAt(LocalDateTime.now());
        } else if ("DELIVERED".equalsIgnoreCase(status)) {
            d.setDeliveredAt(LocalDateTime.now());
        }

        deliveryRepository.save(d);
        return toResponse(d);
    }
    @Transactional
    public DeliveryResponse reportFailed(Long deliveryId, String reason) {
        Delivery d = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        d.setDeliveryNotes(reason);
        deliveryRepository.save(d);
        UpdateShipmentDTO upd = new UpdateShipmentDTO();
        upd.setShipmentID(d.getShipmentId());
        upd.setStatus(ShipmentStatus.FAILED);
        shipmentClient.updateStatus(upd);

        return toResponse(d);
    }

    // helper
    private DeliveryResponse toResponse(Delivery d) {
        Long driverId = d.getDriver() != null ? d.getDriver().getId() : null;
        return new DeliveryResponse(
                d.getId(),
                d.getShipmentId(),
                driverId,
                d.getAssignedAt(),
                d.getAcceptedAt(),
                d.getDeliveredAt(),
                d.getPickedUpAt(),
                d.getRecipientName(),
                d.getDeliveryNotes()
        );
    }


}