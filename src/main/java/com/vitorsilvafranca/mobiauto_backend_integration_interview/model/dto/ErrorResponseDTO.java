package com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto;

public class ErrorResponseDTO {

    private final String mensagem;

    public ErrorResponseDTO(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}