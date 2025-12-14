package com.ash7nly.monolith.service;

import com.ash7nly.monolith.dto.request.ProcessPaymentRequest;
import com.ash7nly.monolith.dto.response.PaymentResponse;
import com.ash7nly.monolith.entity.Payment;
import com.ash7nly.monolith.entity.ShipmentEntity;
import com.ash7nly.monolith.enums.PaymentStatus;
import com.ash7nly.monolith.exception.NotFoundException;
import com.ash7nly.monolith.repository.PaymentRepository;
import com.ash7nly.monolith.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ShipmentRepository shipmentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, ShipmentRepository shipmentRepository) {
        this.paymentRepository = paymentRepository;
        this.shipmentRepository = shipmentRepository;
    }

    /**
     * Creates a payment record for a shipment. Called when a shipment is created.
     */
    @Transactional
    public Payment createPaymentForShipment(ShipmentEntity shipment) {
        Payment payment = new Payment();
        payment.setShipment(shipment);
        payment.setAmount(shipment.getCost());
        payment.setStatus(PaymentStatus.PENDING);
        return paymentRepository.save(payment);
    }

    /**
     * Get payment details by payment ID
     */
    public PaymentResponse getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found with ID: " + paymentId));
        return toPaymentResponse(payment);
    }

    /**
     * Get payment by shipment ID
     */
    public PaymentResponse getPaymentByShipmentId(Long shipmentId) {
        Payment payment = paymentRepository.findByShipment_ShipmentId(shipmentId)
                .orElseThrow(() -> new NotFoundException("Payment not found for shipment ID: " + shipmentId));
        return toPaymentResponse(payment);
    }

    /**
     * Process payment - fakes a transaction and marks shipment as paid
     */
    @Transactional
    public PaymentResponse processPayment(ProcessPaymentRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new NotFoundException("Payment not found with ID: " + request.getPaymentId()));

        // Check if payment is already completed
        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            throw new RuntimeException("Payment has already been processed");
        }

        // Fake transaction processing - simulate a successful payment
        String transactionId = generateTransactionId();
        String lastFourDigits = request.getCardNumber().substring(request.getCardNumber().length() - 4);

        // Update payment status
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setTransactionId(transactionId);
        payment.setCardLastFourDigits(lastFourDigits);
        payment.setPaidAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        // Mark shipment as paid/active so drivers can see it
        ShipmentEntity shipment = payment.getShipment();
        shipment.setActive(true);
        shipmentRepository.save(shipment);

        return toPaymentResponse(savedPayment);
    }

    /**
     * Generate a fake transaction ID
     */
    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Convert Payment entity to PaymentResponse DTO
     */
    private PaymentResponse toPaymentResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getPaymentId());
        response.setShipmentId(payment.getShipment().getShipmentId());
        response.setTrackingNumber(payment.getShipment().getTrackingNumber());
        response.setAmount(payment.getAmount());
        response.setStatus(payment.getStatus());
        response.setTransactionId(payment.getTransactionId());
        response.setCardLastFourDigits(payment.getCardLastFourDigits());
        response.setCreatedAt(payment.getCreatedAt());
        response.setPaidAt(payment.getPaidAt());
        return response;
    }
}

