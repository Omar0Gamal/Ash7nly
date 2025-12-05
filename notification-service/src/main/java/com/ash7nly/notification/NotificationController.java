package com.ash7nly.notification;

import com.ash7nly.notification.dto.NotificationRequest;
import com.ash7nly.notification.dto.NotificationResponse;
import com.ash7nly.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this. notificationService = notificationService;
    }


    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
                "service", "notification-service",
                "status", "UP"
        );
    }

    @PostMapping("/shipment-created")
    public ResponseEntity<NotificationResponse> sendShipmentNotification(
            @RequestBody NotificationRequest request) {

        try {
            NotificationResponse response = notificationService.sendShipmentNotification(request);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            NotificationResponse errorResponse = new NotificationResponse(false,
                    "Failed to send notification: " + e. getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}

