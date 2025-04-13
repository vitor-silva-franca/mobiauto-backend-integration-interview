package com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception;

public class EntityNotCreatedException extends RuntimeException {
    public EntityNotCreatedException(String message) {
        super(message);
    }
}
