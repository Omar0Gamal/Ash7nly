package com.ash7nly.monolith.exception;

import org.springframework.http.HttpStatus;

public class PaymentAlreadyProcessedException extends ApiException {
    public PaymentAlreadyProcessedException(Long paymentId) {
        super("Payment " + paymentId + " has already been processed", HttpStatus.CONFLICT);
    }
}

