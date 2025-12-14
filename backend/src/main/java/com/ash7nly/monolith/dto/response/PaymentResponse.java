package com.ash7nly.monolith.dto.response;

import com.ash7nly.monolith.enums.PaymentStatus;

import java.time.LocalDateTime;

public class PaymentResponse {

    private Long paymentId;
    private Long shipmentId;
    private String trackingNumber;
    private double amount;
    private PaymentStatus status;
    private String transactionId;
    private String cardLastFourDigits;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;

    // -------------------------
    // Constructors
    // -------------------------

    public PaymentResponse() {
    }

    public PaymentResponse(Long paymentId, Long shipmentId, String trackingNumber, double amount,
                           PaymentStatus status, String transactionId, String cardLastFourDigits,
                           LocalDateTime createdAt, LocalDateTime paidAt) {
        this.paymentId = paymentId;
        this.shipmentId = shipmentId;
        this.trackingNumber = trackingNumber;
        this.amount = amount;
        this.status = status;
        this.transactionId = transactionId;
        this.cardLastFourDigits = cardLastFourDigits;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
    }

    // -------------------------
    // Getters & Setters
    // -------------------------

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCardLastFourDigits() {
        return cardLastFourDigits;
    }

    public void setCardLastFourDigits(String cardLastFourDigits) {
        this.cardLastFourDigits = cardLastFourDigits;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}

