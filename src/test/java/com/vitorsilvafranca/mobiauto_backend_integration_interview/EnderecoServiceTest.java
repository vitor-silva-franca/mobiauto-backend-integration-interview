package com.vitorsilvafranca.mobiauto_backend_integration_interview;

import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Endereco;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.EnderecoService;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.NominatimService;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.ViaCepService;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.InvalidCepException;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.validator.ValidadorCep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EnderecoServiceTest {

    @InjectMocks
    private EnderecoService enderecoService;

    @Mock
    private ViaCepService viaCepService;

    @Mock
    private NominatimService nominatimService;

    @Mock
    private ValidadorCep validadorCep;

    private Endereco endereco;

    @BeforeEach
    void setUp() {
        endereco = new Endereco();
        endereco.setCep("12345678");
        endereco.setLogradouro("Rua Exemplo");
        endereco.setCidade("São Paulo");
        endereco.setUf("SP");
        endereco.setLatitude(-23.5);
        endereco.setLongitude(-46.6);
    }

    @Test
    void deveGravarEnderecoComSucesso() {
        doNothing().when(validadorCep).validarCep("12345678");
        when(viaCepService.buscarEnderecoPorCep("12345678")).thenReturn(endereco);
        doNothing().when(nominatimService).completarLatitudeLongitude(endereco);

        Endereco resultado = enderecoService.gravarEndereco("12345678");

        assertNotNull(resultado);
        assertEquals("São Paulo", resultado.getCidade());
        verify(validadorCep).validarCep("12345678");
        verify(viaCepService).buscarEnderecoPorCep("12345678");
        verify(nominatimService).completarLatitudeLongitude(endereco);
    }

    @Test
    void deveLancarExcecaoQuandoCepForInvalido() {
        doThrow(new InvalidCepException("CEP inválido")).when(validadorCep).validarCep("000");

        assertThrows(InvalidCepException.class, () -> enderecoService.gravarEndereco("000"));

        verify(validadorCep).validarCep("000");
        verify(viaCepService, never()).buscarEnderecoPorCep(any());
        verify(nominatimService, never()).completarLatitudeLongitude(any());
    }

    @Test
    void deveLancarExcecaoQuandoViaCepFalhar() {
        doNothing().when(validadorCep).validarCep("12345678");
        when(viaCepService.buscarEnderecoPorCep("12345678"))
                .thenThrow(new RuntimeException("Falha ViaCep"));

        assertThrows(RuntimeException.class, () -> enderecoService.gravarEndereco("12345678"));

        verify(validadorCep).validarCep("12345678");
        verify(viaCepService).buscarEnderecoPorCep("12345678");
        verify(nominatimService, never()).completarLatitudeLongitude(any());
    }
}