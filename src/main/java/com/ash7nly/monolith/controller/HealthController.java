package com.ash7nly.monolith.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
                "service", "ash7nly-monolith",
                "status", "UP"
        );
    }

    @GetMapping("/payments/health")
    public Map<String, String> paymentsHealth() {
        return Map.of(
                "module", "payments",
                "status", "UP"
        );
    }

    @GetMapping("/notifications/health")
    public Map<String, String> notificationsHealth() {
        return Map.of(
                "module", "notifications",
                "status", "UP"
        );
    }

    @GetMapping("/analytics/health")
    public Map<String, String> analyticsHealth() {
        return Map.of(
                "module", "analytics",
                "status", "UP"
        );
    }
}

