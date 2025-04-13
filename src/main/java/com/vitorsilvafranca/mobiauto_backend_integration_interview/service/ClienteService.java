package com.vitorsilvafranca.mobiauto_backend_integration_interview.service;

import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Cliente;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Endereco;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto.ClienteDTO;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.mapper.ClienteMapper;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.repository.ClienteRepository;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.EntityNotCreatedException;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.validator.ValidadorPessoaBase;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private ValidadorPessoaBase validadorPessoaBase;

    @Autowired
    private ClienteMapper mapper;

    public ClienteDTO salvarCliente(ClienteDTO clienteDTO) {
        try {
            validadorPessoaBase.validarNome(clienteDTO.getNome());

            Endereco e = enderecoService.gravarEndereco(clienteDTO.getCep());

            Cliente c = mapper.toCliente(clienteDTO);
            c.setEndereco(e);

            clienteRepository.save(c);
            clienteDTO.setId(c.getId());

            return clienteDTO;
        } catch (Exception e) {
            throw new EntityNotCreatedException("Não foi possível registrar o cliente, erro: " + e.getMessage());
        }
    }

    public ClienteDTO buscarClienteDTO(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        return mapper.toClienteDTO(cliente);
    }
}
