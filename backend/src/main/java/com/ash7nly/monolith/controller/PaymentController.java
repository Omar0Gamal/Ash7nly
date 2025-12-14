package com.ash7nly.monolith.controller;

import com.ash7nly.monolith.dto.request.ProcessPaymentRequest;
import com.ash7nly.monolith.dto.response.ApiResponse;
import com.ash7nly.monolith.dto.response.PaymentResponse;
import com.ash7nly.monolith.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Get payment details by payment ID
     */
    @GetMapping("/{paymentId}")
    public ApiResponse<PaymentResponse> getPayment(@PathVariable Long paymentId) {
        return ApiResponse.success(paymentService.getPaymentById(paymentId));
    }

    /**
     * Get payment by shipment ID
     */
    @GetMapping("/shipment/{shipmentId}")
    public ApiResponse<PaymentResponse> getPaymentByShipment(@PathVariable Long shipmentId) {
        return ApiResponse.success(paymentService.getPaymentByShipmentId(shipmentId));
    }

    /**
     * Process payment - takes payment ID and card info, fakes transaction,
     * marks shipment as paid making it available for drivers
     */
    @PostMapping("/process")
    public ApiResponse<PaymentResponse> processPayment(@Valid @RequestBody ProcessPaymentRequest request) {
        return ApiResponse.success(paymentService.processPayment(request), "Payment processed successfully");
    }
}

