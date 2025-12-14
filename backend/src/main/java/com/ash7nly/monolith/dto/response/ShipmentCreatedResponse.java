package com.ash7nly.monolith.dto.response;

import com.ash7nly.monolith.entity.ShipmentEntity;
import com.ash7nly.monolith.enums.DeliveryArea;
import com.ash7nly.monolith.enums.ShipmentStatus;

import java.time.LocalDateTime;

public class ShipmentCreatedResponse {

    private Long shipmentId;
    private String trackingNumber;
    private String pickupAddress;
    private DeliveryArea deliveryAddress;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String packageWeight;
    private String packageDimension;
    private String packageDescription;
    private Long merchantId;
    private ShipmentStatus status;
    private double cost;
    private boolean isActive;
    private LocalDateTime createdAt;

    // Payment info
    private Long paymentId;
    private String paymentMessage;

    // -------------------------
    // Constructors
    // -------------------------

    public ShipmentCreatedResponse() {
    }

    public ShipmentCreatedResponse(ShipmentEntity shipment, Long paymentId) {
        this.shipmentId = shipment.getShipmentId();
        this.trackingNumber = shipment.getTrackingNumber();
        this.pickupAddress = shipment.getPickupAdress();
        this.deliveryAddress = shipment.getDeliveryAdress();
        this.customerName = shipment.getCustomerName();
        this.customerPhone = shipment.getCustomerphone();
        this.customerEmail = shipment.getCustomerEmail();
        this.packageWeight = shipment.getPackageWeight();
        this.packageDimension = shipment.getPackageDimension();
        this.packageDescription = shipment.getPackageDescription();
        this.merchantId = shipment.getMerchantId();
        this.status = shipment.getStatus();
        this.cost = shipment.getCost();
        this.isActive = shipment.isActive();
        this.createdAt = shipment.getCreatedAt();
        this.paymentId = paymentId;
        this.paymentMessage = "Shipment created. Please complete payment using paymentId to activate the shipment.";
    }

    // -------------------------
    // Getters & Setters
    // -------------------------

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

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public DeliveryArea getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryArea deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(String packageWeight) {
        this.packageWeight = packageWeight;
    }

    public String getPackageDimension() {
        return packageDimension;
    }

    public void setPackageDimension(String packageDimension) {
        this.packageDimension = packageDimension;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentMessage() {
        return paymentMessage;
    }

    public void setPaymentMessage(String paymentMessage) {
        this.paymentMessage = paymentMessage;
    }
}

