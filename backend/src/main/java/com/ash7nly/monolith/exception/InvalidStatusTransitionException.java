package com.ash7nly.monolith.exception;

import org.springframework.http.HttpStatus;

public class InvalidStatusTransitionException extends ApiException {
    public InvalidStatusTransitionException(String currentStatus, String newStatus) {
        super("Invalid status transition from " + currentStatus + " to " + newStatus, HttpStatus.BAD_REQUEST);
    }
}

