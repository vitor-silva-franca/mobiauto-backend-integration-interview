package com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
