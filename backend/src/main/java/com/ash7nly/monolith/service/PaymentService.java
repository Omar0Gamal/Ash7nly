package com.ash7nly.monolith.service;

import com.ash7nly.monolith.dto.request.ProcessPaymentRequest;
import com.ash7nly.monolith.dto.response.PaymentResponse;
import com.ash7nly.monolith.entity.Payment;
import com.ash7nly.monolith.entity.Shipment;
import com.ash7nly.monolith.enums.PaymentStatus;
import com.ash7nly.monolith.exception.ForbiddenException;
import com.ash7nly.monolith.exception.NotFoundException;
import com.ash7nly.monolith.exception.PaymentAlreadyProcessedException;
import com.ash7nly.monolith.repository.PaymentRepository;
import com.ash7nly.monolith.repository.ShipmentRepository;
import com.ash7nly.monolith.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final String TRANSACTION_PREFIX = "TXN-";
    private static final int CARD_LAST_DIGITS_LENGTH = 4;

    private final PaymentRepository paymentRepository;
    private final ShipmentRepository shipmentRepository;
    private final CurrentUserService currentUserService;

    @Transactional
    public Payment createPaymentForShipment(Shipment shipment) {
        Payment payment = new Payment();
        payment.setShipment(shipment);
        payment.setAmount(shipment.getCost());
        payment.setStatus(PaymentStatus.PENDING);

        Payment saved = paymentRepository.save(payment);
        log.info("Payment created for shipment: {}", shipment.getTrackingNumber());
        return saved;
    }

    public PaymentResponse getPaymentById(Long paymentId, Long merchantId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found with id: " + paymentId));

        if (!currentUserService.isAdmin()) {
            Shipment shipment = payment.getShipment();
            if (!shipment.getMerchant().getId().equals(merchantId)) {
                throw new ForbiddenException("Access denied: payment not owned by you");
            }
        }

        return toPaymentResponse(payment);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByShipmentId(Long shipmentId, Long merchantId) {
        Payment payment = paymentRepository.findByShipment_ShipmentId(shipmentId)
                .orElseThrow(() -> new NotFoundException("Payment not found for shipment id: " + shipmentId));

        if (!currentUserService.isAdmin()) {
            Shipment shipment = payment.getShipment();
            if (!shipment.getMerchant().getId().equals(merchantId)) {
                throw new ForbiddenException("Access denied: payment not owned by you");
            }
        }

        return toPaymentResponse(payment);
    }

    @Transactional
    public PaymentResponse processPayment(Long paymentId, ProcessPaymentRequest request, Long merchantId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found with id: " + paymentId));

        if (!currentUserService.isAdmin()) {
            Shipment shipment = payment.getShipment();
            if (!shipment.getMerchant().getId().equals(merchantId)) {
                throw new ForbiddenException("Access denied: payment not owned by you");
            }
        }

        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            throw new PaymentAlreadyProcessedException(paymentId);
        }

        String transactionId = generateTransactionId();
        String lastFourDigits = extractLastFourDigits(request.getCardNumber());

        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setTransactionId(transactionId);
        payment.setCardLastFourDigits(lastFourDigits);
        payment.setPaidAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        Shipment shipment = payment.getShipment();
        shipment.setActive(true);
        shipmentRepository.save(shipment);

        log.info("Payment {} processed successfully for shipment {}: {}",
                payment.getPaymentId(), shipment.getShipmentId(), transactionId);

        return toPaymentResponse(savedPayment);
    }

    private String generateTransactionId() {
        return TRANSACTION_PREFIX + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String extractLastFourDigits(String cardNumber) {
        return cardNumber.substring(cardNumber.length() - CARD_LAST_DIGITS_LENGTH);
    }

    private PaymentResponse toPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .shipmentId(payment.getShipment().getShipmentId())
                .trackingNumber(payment.getShipment().getTrackingNumber())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .cardLastFourDigits(payment.getCardLastFourDigits())
                .createdAt(payment.getCreatedAt())
                .paidAt(payment.getPaidAt())
                .build();
    }
}
