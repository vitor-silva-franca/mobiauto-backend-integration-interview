package com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
