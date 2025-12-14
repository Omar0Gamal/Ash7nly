package com.ash7nly.monolith.entity;

import com.ash7nly.monolith.enums.PaymentStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne
    @JoinColumn(name = "shipment_id", nullable = false, unique = true)
    private ShipmentEntity shipment;

    @Column(nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private String transactionId;

    private String cardLastFourDigits;

    private LocalDateTime createdAt;
    private LocalDateTime paidAt;

    // -------------------------
    // Constructors
    // -------------------------

    public Payment() {
    }

    public Payment(Long paymentId, ShipmentEntity shipment, double amount, PaymentStatus status,
                   String transactionId, String cardLastFourDigits, LocalDateTime createdAt, LocalDateTime paidAt) {
        this.paymentId = paymentId;
        this.shipment = shipment;
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

    public ShipmentEntity getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentEntity shipment) {
        this.shipment = shipment;
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

    // -------------------------
    // Lifecycle Hooks
    // -------------------------

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
