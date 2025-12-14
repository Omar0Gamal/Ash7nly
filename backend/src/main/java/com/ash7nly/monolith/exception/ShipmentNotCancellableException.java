package com.ash7nly.monolith.exception;

import org.springframework.http.HttpStatus;

public class ShipmentNotCancellableException extends ApiException {
    public ShipmentNotCancellableException(String reason) {
        super("Shipment cannot be cancelled: " + reason, HttpStatus.BAD_REQUEST);
    }
}

