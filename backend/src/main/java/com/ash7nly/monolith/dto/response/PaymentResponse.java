package com.ash7nly.monolith.dto.response;

import com.ash7nly.monolith.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}

