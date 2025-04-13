package com.vitorsilvafranca.mobiauto_backend_integration_interview.service;

import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Endereco;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.validator.ValidadorCep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {

    @Autowired
    private ViaCepService viaCepService;

    @Autowired
    private NominatimService nominatimService;

    @Autowired
    private ValidadorCep validadorCep;

    public Endereco gravarEndereco(String cep) {
        validadorCep.validarCep(cep);

        Endereco endereco = viaCepService.buscarEnderecoPorCep(cep);
        nominatimService.completarLatitudeLongitude(endereco);

        return endereco;
    }
}
