package com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception;

public class LatitudeOrLongitudeInvalidException extends RuntimeException {
    public LatitudeOrLongitudeInvalidException(String message) {
        super(message);
    }
}
