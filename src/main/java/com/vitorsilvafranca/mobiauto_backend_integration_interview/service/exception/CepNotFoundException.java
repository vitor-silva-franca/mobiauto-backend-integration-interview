package com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception;

public class CepNotFoundException extends RuntimeException {
    public CepNotFoundException(String message) {
        super(message);
    }
}
