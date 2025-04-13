package com.vitorsilvafranca.mobiauto_backend_integration_interview.service;

import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Anunciante;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Endereco;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto.AnuncianteDTO;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.mapper.AnuncianteMapper;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.repository.AnuncianteRepository;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.EntityNotCreatedException;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.validator.ValidadorPessoaBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnuncianteService {

    @Autowired
    private AnuncianteRepository anuncianteRepository;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private ValidadorPessoaBase validadorPessoaBase;

    @Autowired
    private AnuncianteMapper mapper;

    public AnuncianteDTO salvarAnunciante(AnuncianteDTO anuncianteDTO) {
        try {
            validadorPessoaBase.validarNome(anuncianteDTO.getNome());

            Endereco e = enderecoService.gravarEndereco(anuncianteDTO.getCep());

            Anunciante a = mapper.toAnunciante(anuncianteDTO);
            a.setEndereco(e);

            anuncianteRepository.save(a);
            anuncianteDTO.setId(a.getId());

            return anuncianteDTO;
        } catch (Exception e) {
            throw new EntityNotCreatedException("Não foi possível registrar o anunciante, erro: " + e.getMessage());
        }
    }

    public List<Anunciante> buscarTodos() {
        return new ArrayList<>(anuncianteRepository.findAll());
    }
}
