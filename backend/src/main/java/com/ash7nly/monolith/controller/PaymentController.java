package com.ash7nly.monolith.controller;

import com.ash7nly.monolith.dto.request.ProcessPaymentRequest;
import com.ash7nly.monolith.dto.response.ApiResponse;
import com.ash7nly.monolith.dto.response.PaymentResponse;
import com.ash7nly.monolith.security.CurrentUserService;
import com.ash7nly.monolith.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final CurrentUserService currentUserService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MERCHANT', 'ADMIN')")
    public ApiResponse<PaymentResponse> getPayment(@PathVariable Long id) {
        return ApiResponse.success(paymentService.getPaymentById(id, currentUserService.getCurrentUserId()));
    }

    @GetMapping("/shipment/{shipmentId}")
    @PreAuthorize("hasAnyAuthority('MERCHANT', 'ADMIN')")
    public ApiResponse<PaymentResponse> getPaymentByShipment(@PathVariable Long shipmentId) {
        return ApiResponse.success(paymentService.getPaymentByShipmentId(shipmentId, currentUserService.getCurrentUserId()));
    }

    @PostMapping("/{id}/process")
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ApiResponse<PaymentResponse> processPayment(
            @PathVariable Long id,
            @Valid @RequestBody ProcessPaymentRequest request) {
        return ApiResponse.success(paymentService.processPayment(id, request, currentUserService.getCurrentUserId()));
    }
}
