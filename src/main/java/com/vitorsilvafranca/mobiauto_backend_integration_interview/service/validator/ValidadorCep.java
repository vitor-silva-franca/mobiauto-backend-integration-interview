package com.vitorsilvafranca.mobiauto_backend_integration_interview.service.validator;

import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.InvalidCepException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorCep {

    public void validarCep(String cep) {
        if (cep == null || !cep.matches("^\\d{8}$")) {
            throw new InvalidCepException("CEP inválido, utilize somente os 8 dígitos numéricos.");
        }
    }
}
