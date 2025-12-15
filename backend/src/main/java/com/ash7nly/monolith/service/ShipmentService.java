package com.ash7nly.monolith.service;

import com.ash7nly.monolith.dto.request.CreateShipmentRequest;
import com.ash7nly.monolith.dto.response.*;
import com.ash7nly.monolith.enums.DeliveryArea;
import com.ash7nly.monolith.enums.ShipmentStatus;
import com.ash7nly.monolith.exception.ForbiddenException;
import com.ash7nly.monolith.exception.NotFoundException;
import com.ash7nly.monolith.exception.ShipmentNotCancellableException;
import com.ash7nly.monolith.entity.*;
import com.ash7nly.monolith.mapper.*;
import com.ash7nly.monolith.repository.*;
import com.ash7nly.monolith.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;
    private final TrackingMapper trackingMapper;
    private final TrackingHistoryRepository trackingHistoryRepository;
    private final DeliveryRepository deliveryRepository;
    private final NotificationService notificationService;
    private final PaymentService paymentService;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;

    public ShipmentTrackingResponse getTrackingInfo(String trackingNumber) {
        Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new NotFoundException("Tracking code not found: " + trackingNumber));
        Delivery delivery = shipment.getDelivery();

        List<TrackingHistoryResponse> trackingHistoryList = trackingHistoryRepository
                .findByShipmentOrderByTimestampAsc(shipment)
                .stream()
                .map(trackingMapper::toResponse)
                .toList();

        if (delivery == null || delivery.getDriver() == null) {
            return ShipmentTrackingResponse.builder()
                    .trackingHistoryList(trackingHistoryList)
                    .driverPhone("N/A")
                    .driverName("N/A")
                    .driverEmail("N/A")
                    .build();
        }

        Driver driver = delivery.getDriver();
        if (driver == null || driver.getUser() == null) {
            return ShipmentTrackingResponse.builder()
                    .trackingHistoryList(trackingHistoryList)
                    .driverPhone("N/A")
                    .driverName("N/A")
                    .driverEmail("N/A")
                    .build();
        }

        return ShipmentTrackingResponse.builder()
                .trackingHistoryList(trackingHistoryList)
                .driverPhone(driver.getUser().getPhoneNumber())
                .driverName(driver.getUser().getFullName())
                .driverEmail(driver.getUser().getEmail())
                .build();
    }

    @Transactional
    public ShipmentCreatedResponse createShipment(CreateShipmentRequest request, Long merchantId) {
        log.info("Creating shipment for merchant: {}", merchantId);

        User merchant = userRepository.findById(merchantId)
                .orElseThrow(() -> new NotFoundException("Merchant not found with id: " + merchantId));

        Shipment shipment = shipmentMapper.toEntity(request, merchant);
        shipment.setActive(false);
        Shipment saved = shipmentRepository.save(shipment);

        TrackingHistory history = new TrackingHistory();
        history.setShipment(shipment);
        history.setShipmentStatus(ShipmentStatus.CREATED);
        trackingHistoryRepository.save(history);

        Delivery delivery = new Delivery();
        delivery.setShipment(shipment);
        delivery.setRecipientName(shipment.getCustomerName());
        delivery.setDriver(null);
        deliveryRepository.save(delivery);
        shipment = shipmentRepository.save(shipment);

        Payment savedPayment = paymentService.createPaymentForShipment(saved);

        log.info("Shipment created with tracking number: {}", shipment.getTrackingNumber());

        sendShipmentCreatedNotification(shipment);

        return ShipmentCreatedResponse.builder()
                .shipmentId(saved.getShipmentId())
                .trackingNumber(saved.getTrackingNumber())
                .pickupAddress(saved.getPickupAddress())
                .deliveryAddress(saved.getDeliveryAddress())
                .customerName(saved.getCustomerName())
                .customerPhone(saved.getCustomerPhone())
                .customerEmail(saved.getCustomerEmail())
                .packageWeight(saved.getPackageWeight())
                .packageDimension(saved.getPackageDimension())
                .packageDescription(saved.getPackageDescription())
                .merchantId(saved.getMerchant().getId())
                .status(saved.getStatus())
                .cost(saved.getCost())
                .isActive(saved.isActive())
                .createdAt(saved.getCreatedAt())
                .paymentId(savedPayment.getPaymentId())
                .paymentMessage("Payment record created successfully")
                .build();
    }

    private void sendShipmentCreatedNotification(Shipment shipment) {
        try {
            String emailAddress = Objects.requireNonNullElse(
                    shipment.getCustomerEmail(),
                    "test@test.com"
            );
            notificationService.sendShipmentCreatedNotification(shipment, emailAddress);
            log.info("Notification sent for shipment: {}", shipment.getTrackingNumber());
        } catch (Exception e) {
            log.error("Failed to send notification for shipment {}: {}",
                    shipment.getTrackingNumber(), e.getMessage());
        }
    }

    @Transactional
    public CancelShipmentResponse cancelShipment(Long shipmentId, Long merchantId, String reason) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new NotFoundException("Shipment not found"));

        if (!currentUserService.isAdmin() && !shipment.getMerchant().getId().equals(merchantId)) {
            throw new ForbiddenException("You can only cancel your own shipments");
        }

        if (!shipment.isCancellable()) {
            throw new ShipmentNotCancellableException("Shipment cannot be cancelled");
        }

        shipment.setStatus(ShipmentStatus.CANCELLED);
        shipmentRepository.save(shipment);

        return new CancelShipmentResponse(shipmentId, shipment.getTrackingNumber(), "CANCELLED", "Shipment cancelled successfully");
    }

    @Transactional
    public UpdateShipmentStatusResponse updateShipmentStatus(long shipmentId, ShipmentStatus newStatus) {
        Shipment shipment = shipmentRepository.findByShipmentId(shipmentId)
                .orElseThrow(() -> new NotFoundException("Shipment not found with id: " + shipmentId));

        shipment.setStatus(newStatus);
        shipment.setUpdatedAt(LocalDateTime.now());
        Shipment savedShipment = shipmentRepository.save(shipment);

        TrackingHistory history = new TrackingHistory();
        history.setShipment(shipment);
        history.setShipmentStatus(newStatus);
        trackingHistoryRepository.save(history);

        log.info("Shipment {} status updated to {}", shipmentId, newStatus);
        return shipmentMapper.toUpdateResponse(savedShipment);
    }

    public ShipmentListResponse getShipmentById(long shipmentId, Long requestingUserId) {
        Shipment shipment = shipmentRepository.findByShipmentId(shipmentId)
                .orElseThrow(() -> new NotFoundException("Shipment not found with id: " + shipmentId));

        if (!currentUserService.isAdmin() && !shipment.getMerchant().getId().equals(requestingUserId)) {
            throw new ForbiddenException("Access denied");
        }

        return shipmentMapper.toResponse(shipment);
    }

    @Transactional(readOnly = true)
    public MerchantShipmentsResponse getShipmentsByMerchantId(long merchantId) {
        List<Shipment> shipments = shipmentRepository.findByMerchant_Id(merchantId);
        List<ShipmentListResponse> shipmentList = shipments.stream()
                .map(shipmentMapper::toResponse)
                .toList();
        long count = shipmentRepository.countByMerchant_Id(merchantId);
        return MerchantShipmentsResponse.builder()
                .shipments(shipmentList)
                .totalCount(count)
                .build();
    }

}
