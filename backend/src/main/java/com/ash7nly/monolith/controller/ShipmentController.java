package com.ash7nly.monolith.controller;

import com.ash7nly.monolith.dto.request.CancelShipmentRequest;
import com.ash7nly.monolith.dto.request.CreateShipmentRequest;
import com.ash7nly.monolith.dto.response.*;
import com.ash7nly.monolith.security.CurrentUserService;
import com.ash7nly.monolith.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final CurrentUserService currentUserService;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ApiResponse<ShipmentCreatedResponse> createShipment(@Valid @RequestBody CreateShipmentRequest request) {
        return ApiResponse.success(shipmentService.createShipment(request, currentUserService.getCurrentUserId()));
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ApiResponse<MerchantShipmentsResponse> getMerchantShipments() {
        Long merchantId = currentUserService.getCurrentUserId();
        return ApiResponse.success(shipmentService.getShipmentsByMerchantId(merchantId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MERCHANT', 'ADMIN')")
    public ApiResponse<ShipmentListResponse> getShipmentById(@PathVariable long id) {
        return ApiResponse.success(shipmentService.getShipmentById(id, currentUserService.getCurrentUserId()));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ApiResponse<CancelShipmentResponse> cancelShipment(
            @PathVariable Long id,
            @Valid @RequestBody CancelShipmentRequest request) {
        return ApiResponse.success(
                shipmentService.cancelShipment(id, currentUserService.getCurrentUserId(), request.getCancellationReason())
        );
    }

    @GetMapping("/track/{trackingNumber}")
    public ApiResponse<ShipmentTrackingResponse> getTrackingHistory(@PathVariable String trackingNumber) {
        return ApiResponse.success(shipmentService.getTrackingInfo(trackingNumber));
    }
}