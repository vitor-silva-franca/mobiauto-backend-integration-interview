package com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception;

public class InvalidCepException extends RuntimeException {
    public InvalidCepException(String message) {
        super(message);
    }
}
