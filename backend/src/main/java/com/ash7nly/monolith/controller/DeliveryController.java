package com.ash7nly.monolith.controller;

import com.ash7nly.monolith.dto.request.FailShipmentRequest;
import com.ash7nly.monolith.dto.request.UpdateDeliveryStatusRequest;
import com.ash7nly.monolith.dto.response.*;
import com.ash7nly.monolith.enums.ShipmentStatus;
import com.ash7nly.monolith.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/")
    @PreAuthorize("hasAuthority('DRIVER')")
    public ApiResponse<List<DeliveryResponse>> getMyDeliveries(
            @RequestParam(required = false) ShipmentStatus status) {
        return ApiResponse.success(deliveryService.getMyDeliveries(status));
    }

    @GetMapping("/available")
    @PreAuthorize("hasAuthority('DRIVER')")
    public ApiResponse<List<AvailableDeliveryResponse>> getAvailableDeliveries() {
        return ApiResponse.success(deliveryService.getAvailableDeliveries());
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('DRIVER')")
    public ApiResponse<ActiveDeliveryResponse> getActiveDeliveries() {
        return ApiResponse.success(deliveryService.getMyActiveDeliveries());
    }

    @GetMapping("/history")
    @PreAuthorize("hasAuthority('DRIVER')")
    public ApiResponse<DeliveryHistoryResponse> getCompletedDeliveries() {
        return ApiResponse.success(deliveryService.getMyCompletedDeliveries());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('DRIVER', 'ADMIN')")
    public ApiResponse<DeliveryResponse> getDeliveryById(@PathVariable Long id) {
        return ApiResponse.success(deliveryService.getDeliveryById(id));
    }

    @PostMapping("/{id}/accept")
    @PreAuthorize("hasAuthority('DRIVER')")
    public ApiResponse<DeliveryResponse> acceptDelivery(@PathVariable Long id) {
        return ApiResponse.success(deliveryService.acceptDelivery(id), "Delivery accepted successfully");
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('DRIVER')")
    public ApiResponse<DeliveryResponse> updateDeliveryStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDeliveryStatusRequest request) {
        return ApiResponse.success(deliveryService.updateDeliveryStatus(id, request));
    }

    @PostMapping("/{id}/fail")
    @PreAuthorize("hasAuthority('DRIVER')")
    public ApiResponse<DeliveryResponse> reportFailed(
            @PathVariable Long id,
            @Valid @RequestBody FailShipmentRequest request) {
        return ApiResponse.success(
                deliveryService.reportFailed(id, request.getCancellationReason()),
                "Delivery marked as failed"
        );
    }

    @GetMapping("/assigned")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<List<DeliveryResponse>> getAssignedDeliveries(@RequestParam Long driverId) {
        return ApiResponse.success(deliveryService.getAssignedDeliveries(driverId));
    }
}
