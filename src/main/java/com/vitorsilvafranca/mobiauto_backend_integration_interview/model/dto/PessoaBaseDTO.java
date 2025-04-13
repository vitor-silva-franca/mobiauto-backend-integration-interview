package com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public abstract class PessoaBaseDTO {

    @NotBlank
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "O nome contém caracteres inválidos.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    private String nome;

    @NotBlank
    @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 dígitos.")
    private String cep;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
