package com.ash7nly.monolith.service;

import com.ash7nly.monolith.dto.request.UpdateDeliveryStatusRequest;
import com.ash7nly.monolith.dto.response.ActiveDeliveryResponse;
import com.ash7nly.monolith.dto.response.AvailableDeliveryResponse;
import com.ash7nly.monolith.dto.response.*;
import com.ash7nly.monolith.entity.Delivery;
import com.ash7nly.monolith.entity.Driver;
import com.ash7nly.monolith.entity.Shipment;
import com.ash7nly.monolith.enums.ShipmentStatus;
import com.ash7nly.monolith.exception.BadRequestException;
import com.ash7nly.monolith.exception.ForbiddenException;
import com.ash7nly.monolith.exception.InvalidStatusTransitionException;
import com.ash7nly.monolith.exception.NotFoundException;
import com.ash7nly.monolith.mapper.DeliveryMapper;
import com.ash7nly.monolith.mapper.ShipmentMapper;
import com.ash7nly.monolith.repository.DeliveryRepository;
import com.ash7nly.monolith.repository.DriverRepository;
import com.ash7nly.monolith.repository.ShipmentRepository;
import com.ash7nly.monolith.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DriverRepository driverRepository;
    private final ShipmentRepository shipmentRepository;
    private final DeliveryMapper deliveryMapper;
    private final CurrentUserService currentUserService;
    private final NotificationService notificationService;
    private final ShipmentMapper shipmentMapper;
    private final ShipmentService shipmentService;

    @Transactional
    public DeliveryResponse acceptDelivery(Long deliveryId) {
        Long userId = currentUserService.getCurrentUserId();

        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Driver profile not found for user id: " + userId));

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NotFoundException("Delivery not found with id: " + deliveryId));

        if (delivery.getDriver() != null) {
            throw new BadRequestException("Delivery is already assigned to another driver");
        }

        Shipment shipment = delivery.getShipment();
        if (!shipment.getDeliveryAddress().toString().equalsIgnoreCase(driver.getServiceArea().toString())) {
            throw new BadRequestException("Shipment delivery area does not match your service area");
        }

        delivery.setDriver(driver);
        delivery.setAssignedAt(LocalDateTime.now());
        delivery.setAcceptedAt(LocalDateTime.now());

        shipment.setStatus(ShipmentStatus.ASSIGNED);

        deliveryRepository.save(delivery);
        shipmentRepository.save(shipment);

        log.info("Delivery {} accepted by driver {}", deliveryId, driver.getId());
        return deliveryMapper.toResponse(delivery);
    }

    public DeliveryResponse getDeliveryById(Long deliveryId) {
        Long userId = currentUserService.getCurrentUserId();
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NotFoundException("Delivery not found"));

        if (!currentUserService.isAdmin()) {
            Driver driver = driverRepository.findByUserId(userId)
                    .orElseThrow(() -> new NotFoundException("Driver not found"));

            if (delivery.getDriver() == null ||
                    !delivery.getDriver().getId().equals(driver.getId())) {
                throw new ForbiddenException("Access denied");
            }
        }

        return deliveryMapper.toResponse(delivery);
    }
    @Transactional(readOnly = true)
    public ActiveDeliveryResponse getMyActiveDeliveries() {
        Long userId = currentUserService.getCurrentUserId();

        if (!currentUserService.isDriver()) {
            throw new ForbiddenException("Only drivers can access active deliveries");
        }

        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Driver profile not found for user id: " + userId));

        List<ShipmentStatus> excludedStatuses = java.util.Arrays.asList(
                ShipmentStatus.DELIVERED,
                ShipmentStatus.CANCELLED,
                ShipmentStatus.FAILED
        );

        List<Delivery> deliveries = deliveryRepository.findActiveDeliveriesByDriverId(
                driver.getId(), excludedStatuses);

        Long activeCount = deliveryRepository.countActiveDeliveriesByDriverId(
                driver.getId(), excludedStatuses);

        List<DeliveryResponse> responses = deliveries.stream()
                .map(deliveryMapper::toResponse)
                .toList();

        return ActiveDeliveryResponse.builder()
                .deliveries(responses)
                .totalCount(activeCount)
                .build();
    }

    public DeliveryHistoryResponse getMyCompletedDeliveries() {
        Long userId = currentUserService.getCurrentUserId();

        if (!currentUserService.isDriver()) {
            throw new ForbiddenException("Only drivers can access delivery history");
        }

        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Driver profile not found"));

        List<Delivery> deliveries = deliveryRepository.findCompletedDeliveriesByDriverId(driver.getId());
        Long completedCount = deliveryRepository.countCompletedDeliveriesByDriverId(driver.getId());

        List<DeliveryResponse> responses = deliveries.stream()
                .map(deliveryMapper::toResponse)
                .toList();

        return DeliveryHistoryResponse.builder()
                .deliveries(responses)
                .totalCount(completedCount)
                .build();
    }

    public List<DeliveryResponse> getMyDeliveries(ShipmentStatus status) {
        Long userId = currentUserService.getCurrentUserId();

        if (!currentUserService.isDriver()) {
            throw new ForbiddenException("Only drivers can access delivery history");
        }

        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Driver profile not found"));

        List<Delivery> deliveries;
        if (status != null) {
            deliveries = deliveryRepository.findByDriverIdAndStatus(driver.getId(), status);
        } else {
            deliveries = deliveryRepository.findByDriverId(driver.getId());
        }

        return deliveries.stream()
                .map(deliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DeliveryResponse> getAssignedDeliveries(Long driverId) {
        return deliveryRepository.findByDriverIdAndDeliveredAtIsNull(driverId).stream()
                .map(deliveryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeliveryResponse reportFailed(Long deliveryId, String reason) {
        Delivery delivery = findDeliveryById(deliveryId);

        // Validate driver access only if driver is assigned
        if (delivery.getDriver() != null) {
            validateDriverAccess(delivery.getDriver().getId());
        } else {
            throw new BadRequestException("Cannot report failed: delivery has no driver assigned");
        }

        delivery.setDeliveryNotes(reason);
        if (delivery.getShipment() != null) {
            delivery.getShipment().setStatus(ShipmentStatus.FAILED);
            shipmentRepository.save(delivery.getShipment());
        }

        delivery = deliveryRepository.save(delivery);
        log.info("Delivery {} marked as failed", deliveryId);
        return deliveryMapper.toResponse(delivery);
    }

    private Delivery findDeliveryById(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Delivery not found with id: " + id));
    }

    private void validateDriverAccess(Long driverId) {
        if (currentUserService.isAdmin()) {
            return;
        }

        Long currentUserId = currentUserService.getCurrentUserId();
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new NotFoundException("Driver not found"));

        if (driver.getUser() == null || !driver.getUser().getId().equals(currentUserId)) {
            throw new ForbiddenException("Access denied");
        }
    }

    @Transactional(readOnly = true)
    public List<AvailableDeliveryResponse> getAvailableDeliveries() {
        Long userId = currentUserService.getCurrentUserId();
        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Driver profile not found for user id: " + userId));

        List<Delivery> deliveries = driverRepository.findAvailableDeliveriesForDriver(driver.getId());
        return deliveries.stream()
                .map(deliveryMapper::toAvailableDeliveryResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeliveryResponse updateDeliveryStatus(Long deliveryId, UpdateDeliveryStatusRequest request) {
        Long userId = currentUserService.getCurrentUserId();

        if (!currentUserService.isDriver()) {
            throw new ForbiddenException("Only drivers can update delivery status");
        }

        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Driver profile not found for user id: " + userId));

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NotFoundException("Delivery not found with id: " + deliveryId));

        if (delivery.getDriver() == null || !delivery.getDriver().getId().equals(driver.getId())) {
            throw new ForbiddenException("Access denied: delivery not assigned to you");
        }

        Shipment shipment = delivery.getShipment();
        validateStatusTransition(shipment.getStatus(), request.getStatus());

        LocalDateTime now = LocalDateTime.now();
        Long shipmentId = shipment.getShipmentId();

        log.debug("Updating shipment {} status to {}", shipmentId, request.getStatus());

        processStatusUpdate(request, delivery, shipment, now, shipmentId);

        if (request.getDeliveryNotes() != null && !request.getDeliveryNotes().isBlank()) {
            delivery.setDeliveryNotes(request.getDeliveryNotes());
        }

        deliveryRepository.save(delivery);
        shipmentRepository.save(shipment);

        sendStatusNotification(delivery, shipment);

        return deliveryMapper.toResponse(delivery);
    }

    private void processStatusUpdate(UpdateDeliveryStatusRequest request, Delivery delivery,
                                     Shipment shipment, LocalDateTime now, Long shipmentId) {
        switch (request.getStatus()) {
            case PICKED_UP -> {
                delivery.setPickedUpAt(now);
                shipment.setStatus(ShipmentStatus.PICKED_UP);
                shipmentService.updateShipmentStatus(shipmentId, ShipmentStatus.PICKED_UP);
            }
            case IN_TRANSIT -> {
                if (delivery.getPickedUpAt() == null) {
                    throw new BadRequestException("Cannot set IN_TRANSIT before PICKED_UP");
                }
                shipment.setStatus(ShipmentStatus.IN_TRANSIT);
                shipmentService.updateShipmentStatus(shipmentId, ShipmentStatus.IN_TRANSIT);
            }
            case DELIVERED -> {
                if (delivery.getPickedUpAt() == null) {
                    throw new BadRequestException("Cannot set DELIVERED before PICKED_UP");
                }
                delivery.setDeliveredAt(now);
                shipment.setStatus(ShipmentStatus.DELIVERED);
                shipment.setActive(false);
                shipmentService.updateShipmentStatus(shipmentId, ShipmentStatus.DELIVERED);
            }
            case FAILED -> {
                delivery.setDeliveredAt(now);
                shipment.setStatus(ShipmentStatus.FAILED);
                shipment.setActive(false);
                shipmentService.updateShipmentStatus(shipmentId, ShipmentStatus.FAILED);
            }
            default -> throw new BadRequestException("Invalid status update: " + request.getStatus());
        }
    }

    private void sendStatusNotification(Delivery delivery, Shipment shipment) {
        try {
            String customerEmail = shipment.getCustomerEmail() != null
                    ? shipment.getCustomerEmail()
                    : "test@test.com";
            notificationService.sendStatusUpdateNotification(delivery, customerEmail);
        } catch (Exception e) {
            log.warn("Failed to send status update email: {}", e.getMessage());
        }
    }

    private void validateStatusTransition(ShipmentStatus currentStatus, ShipmentStatus newStatus) {
        switch (currentStatus) {
            case ASSIGNED -> {
                if (newStatus != ShipmentStatus.PICKED_UP && newStatus != ShipmentStatus.FAILED) {
                    throw new InvalidStatusTransitionException(currentStatus.name(), newStatus.name());
                }
            }
            case PICKED_UP -> {
                if (newStatus != ShipmentStatus.IN_TRANSIT &&
                        newStatus != ShipmentStatus.DELIVERED &&
                        newStatus != ShipmentStatus.FAILED) {
                    throw new InvalidStatusTransitionException(currentStatus.name(), newStatus.name());
                }
            }
            case IN_TRANSIT -> {
                if (newStatus != ShipmentStatus.DELIVERED && newStatus != ShipmentStatus.FAILED) {
                    throw new InvalidStatusTransitionException(currentStatus.name(), newStatus.name());
                }
            }
            default ->
                throw new InvalidStatusTransitionException(currentStatus.name(), newStatus.name());
        }
    }
}

