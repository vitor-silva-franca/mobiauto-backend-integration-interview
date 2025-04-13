package com.vitorsilvafranca.mobiauto_backend_integration_interview.service.validator;

import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorPessoaBase {

    public void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new BadRequestException("O nome é obrigatório.");
        }

        if (nome.length() < 2 || nome.length() > 100) {
            throw new BadRequestException("O nome deve ter entre 2 e 100 caracteres.");
        }

        if (!nome.matches("^[\\p{L} .'-]+$")) {
            throw new BadRequestException("O nome contém caracteres inválidos.");
        }
    }
}
