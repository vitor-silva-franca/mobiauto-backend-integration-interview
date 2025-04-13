package com.vitorsilvafranca.mobiauto_backend_integration_interview;

import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Anunciante;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Endereco;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto.AnuncianteDTO;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.mapper.AnuncianteMapper;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.repository.AnuncianteRepository;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.AnuncianteService;
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
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnuncianteServiceTest {

    @InjectMocks
    private AnuncianteService anuncianteService;

    @Mock
    private AnuncianteRepository anuncianteRepository;

    @Mock
    private EnderecoService enderecoService;

    @Mock
    private ValidadorPessoaBase validadorPessoaBase;

    @Mock
    private AnuncianteMapper mapper;

    private AnuncianteDTO anuncianteDTO;
    private Anunciante anunciante;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        anuncianteDTO = new AnuncianteDTO();
        anuncianteDTO.setNome("Lojista XPTO");
        anuncianteDTO.setCep("87654321");

        anunciante = new Anunciante();
        anunciante.setId(10L);
        anunciante.setNome("Lojista XPTO");

        endereco = new Endereco();
        endereco.setCep("87654321");
        endereco.setCidade("Rio de Janeiro");
        endereco.setUf("RJ");
    }

    @Test
    void deveSalvarAnuncianteComSucesso() {
        doNothing().when(validadorPessoaBase).validarNome(anuncianteDTO.getNome());
        when(enderecoService.gravarEndereco("87654321")).thenReturn(endereco);
        when(mapper.toAnunciante(anuncianteDTO)).thenReturn(anunciante);
        when(anuncianteRepository.save(anunciante)).thenReturn(anunciante);

        AnuncianteDTO salvo = anuncianteService.salvarAnunciante(anuncianteDTO);

        assertNotNull(salvo);
        assertEquals("Lojista XPTO", salvo.getNome());
        verify(validadorPessoaBase).validarNome("Lojista XPTO");
        verify(enderecoService).gravarEndereco("87654321");
        verify(anuncianteRepository).save(anunciante);
    }

    @Test
    void deveLancarExcecaoQuandoNomeInvalido() {
        doThrow(new BadRequestException("Nome inválido"))
                .when(validadorPessoaBase).validarNome(any());

        assertThrows(EntityNotCreatedException.class, () -> anuncianteService.salvarAnunciante(anuncianteDTO));

        verify(validadorPessoaBase).validarNome(anuncianteDTO.getNome());
        verify(anuncianteRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoEnderecoFalhar() {
        doNothing().when(validadorPessoaBase).validarNome(anuncianteDTO.getNome());
        when(enderecoService.gravarEndereco(anuncianteDTO.getCep()))
                .thenThrow(new RuntimeException("Erro ViaCEP"));

        assertThrows(EntityNotCreatedException.class, () -> anuncianteService.salvarAnunciante(anuncianteDTO));

        verify(enderecoService).gravarEndereco("87654321");
        verify(anuncianteRepository, never()).save(any());
    }

    @Test
    void deveRetornarListaDeAnunciantes() {
        when(anuncianteRepository.findAll()).thenReturn(List.of(anunciante));

        List<Anunciante> lista = anuncianteService.buscarTodos();

        assertEquals(1, lista.size());
        assertEquals("Lojista XPTO", lista.get(0).getNome());
        verify(anuncianteRepository).findAll();
    }
}