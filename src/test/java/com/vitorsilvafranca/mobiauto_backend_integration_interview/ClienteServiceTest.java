package com.vitorsilvafranca.mobiauto_backend_integration_interview;

import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Cliente;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Endereco;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto.ClienteDTO;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.mapper.ClienteMapper;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.repository.ClienteRepository;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.ClienteService;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.EnderecoService;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.BadRequestException;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.EntityNotCreatedException;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.validator.ValidadorPessoaBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EnderecoService enderecoService;

    @Mock
    private ValidadorPessoaBase validadorPessoaBase;

    @Mock
    private ClienteMapper clienteMapper;

    private ClienteDTO clienteDTO;
    private Cliente cliente;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        clienteDTO = new ClienteDTO();
        clienteDTO.setNome("Maria da Silva");
        clienteDTO.setCep("12345678");

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Maria da Silva");

        endereco = new Endereco();
        endereco.setCep("12345678");
        endereco.setCidade("São Paulo");
        endereco.setUf("SP");
    }

    @Test
    void deveSalvarClienteComSucesso() {
        doNothing().when(validadorPessoaBase).validarNome(clienteDTO.getNome());
        when(enderecoService.gravarEndereco("12345678")).thenReturn(endereco);
        when(clienteMapper.toCliente(clienteDTO)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        ClienteDTO salvo = clienteService.salvarCliente(clienteDTO);

        assertNotNull(salvo);
        assertEquals("Maria da Silva", salvo.getNome());
        verify(validadorPessoaBase).validarNome("Maria da Silva");
        verify(enderecoService).gravarEndereco("12345678");
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveLancarExceptionQuandoNomeInvalido() {
        doThrow(new BadRequestException("Nome inválido"))
                .when(validadorPessoaBase).validarNome(any());

        assertThrows(EntityNotCreatedException.class, () -> clienteService.salvarCliente(clienteDTO));

        verify(validadorPessoaBase).validarNome(clienteDTO.getNome());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void deveLancarExceptionQuandoEnderecoFalhar() {
        doNothing().when(validadorPessoaBase).validarNome(clienteDTO.getNome());
        when(enderecoService.gravarEndereco(clienteDTO.getCep()))
                .thenThrow(new RuntimeException("Erro ViaCep"));

        assertThrows(EntityNotCreatedException.class, () -> clienteService.salvarCliente(clienteDTO));

        verify(enderecoService).gravarEndereco("12345678");
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void deveRetornarClienteDTOAoBuscarPorId() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toClienteDTO(cliente)).thenReturn(clienteDTO);

        ClienteDTO resultado = clienteService.buscarClienteDTO(1L);

        assertNotNull(resultado);
        assertEquals("Maria da Silva", resultado.getNome());
        verify(clienteRepository).findById(1L);
    }
}