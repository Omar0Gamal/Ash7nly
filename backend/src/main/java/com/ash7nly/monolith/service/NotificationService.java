package com.ash7nly.monolith.service;

import com.ash7nly.monolith.entity.Delivery;
import com.ash7nly.monolith.entity.Shipment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

    private final EmailService emailService;

    public void sendShipmentCreatedNotification(Shipment shipment, String customerEmail) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("trackingNumber", shipment.getTrackingNumber());
        variables.put("customerName", shipment.getCustomerName());
        variables.put("status", shipment.getStatus().toString());
        variables.put("packageWeight", shipment.getPackageWeight());
        variables.put("packageDimension", shipment.getPackageDimension());
        variables.put("deliveryArea", shipment.getDeliveryAddress().toString());
        variables.put("cost", String.format("%.2f", shipment.getCost()));

        log.info("Sending shipment notification to: {}", customerEmail);

        emailService.sendTemplatedEmail(
                customerEmail,
                "Shipment Created - " + shipment.getTrackingNumber(),
                "shipment-created",
                variables
        );
    }

    public void sendStatusUpdateNotification(Delivery delivery, String customerEmail) {
        try {
            if (delivery == null || delivery.getShipment() == null ||
                    customerEmail == null || customerEmail.isBlank()) {
                log.warn("Cannot send status update: invalid parameters");
                return;
            }

            Shipment shipment = delivery.getShipment();

            Map<String, Object> variables = buildStatusUpdateVariables(delivery, shipment);
            String subject = getStatusSubject(shipment.getStatus().toString(), shipment.getTrackingNumber());

            emailService.sendTemplatedEmail(
                    customerEmail,
                    subject,
                    "update-status",
                    variables
            );

            log.info("Status update notification sent for status: {}", shipment.getStatus());

        } catch (Exception e) {
            log.error("Failed to send status update notification: {}", e.getMessage(), e);
        }
    }

    private Map<String, Object> buildStatusUpdateVariables(Delivery delivery, Shipment shipment) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("trackingNumber",
                shipment.getTrackingNumber() != null ? shipment.getTrackingNumber() : "N/A");
        variables.put("customerName",
                shipment.getCustomerName() != null ? shipment.getCustomerName() : "Customer");
        variables.put("status",
                shipment.getStatus() != null ? shipment.getStatus().toString() : "UNKNOWN");
        variables.put("currentLocation",
                shipment.getDeliveryAddress() != null ? shipment.getDeliveryAddress() : "Unknown");
        variables.put("updatedAt",
                java.time.LocalDateTime.now().format(DATE_TIME_FORMATTER));

        if (delivery.getDriver() != null && delivery.getDriver().getUser() != null) {
            variables.put("driverName", delivery.getDriver().getUser().getFullName());
        } else {
            variables.put("driverName", null);
        }

        return variables;
    }

    private String getStatusSubject(String status, String trackingNumber) {
        return switch (status) {
            case "ASSIGNED" -> "Driver Assigned - " + trackingNumber;
            case "PICKED_UP" -> "Package Picked Up - " + trackingNumber;
            case "IN_TRANSIT" -> "Your Package is On The Way - " + trackingNumber;
            case "DELIVERED" -> "Package Delivered! - " + trackingNumber;
            case "FAILED" -> "Delivery Attempt Failed - " + trackingNumber;
            case "CANCELLED" -> "Shipment Cancelled - " + trackingNumber;
            default -> "Shipment Status Update - " + trackingNumber;
        };
    }
}