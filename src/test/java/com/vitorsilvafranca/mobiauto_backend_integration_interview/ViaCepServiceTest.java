package com.vitorsilvafranca.mobiauto_backend_integration_interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Endereco;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.ViaCepService;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.InvalidCepException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.io.IOException;
import java.net.HttpURLConnection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViaCepServiceTest {

    @InjectMocks
    private ViaCepService viaCepService;

    private final ObjectMapper mapper = new ObjectMapper();

    private final String jsonValido = """
        {
          "cep": "01001-000",
          "logradouro": "Praça da Sé",
          "bairro": "Sé",
          "localidade": "São Paulo",
          "uf": "SP"
        }
        """;

    private final String jsonErro = """
        {
          "erro": true
        }
        """;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(viaCepService, "mapper", mapper);
    }

    @Test
    void deveRetornarEnderecoQuandoRespostaValida() throws Exception {
        ViaCepService spy = Mockito.spy(viaCepService);
        doReturn(jsonValido).when(spy).lerResposta(any());

        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(200);
        doReturn(connectionMock).when(spy).abrirConexaoViaCep(any());

        Endereco endereco = spy.buscarEnderecoPorCep("01001000");

        assertEquals("Praça da Sé", endereco.getLogradouro());
        assertEquals("São Paulo", endereco.getCidade());
        assertEquals("SP", endereco.getUf());
    }

    @Test
    void deveLancarExcecaoQuandoStatusHttpNaoFor200() throws Exception {
        ViaCepService spy = Mockito.spy(viaCepService);

        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(500);
        doReturn(connectionMock).when(spy).abrirConexaoViaCep(any());

        assertThrows(InvalidCepException.class, () -> spy.buscarEnderecoPorCep("12345678"));
    }

    @Test
    void deveLancarExcecaoQuandoJsonInvalido() throws Exception {
        ViaCepService spy = Mockito.spy(viaCepService);
        String jsonInvalido = "{json quebrado";

        doReturn(jsonInvalido).when(spy).lerResposta(any());

        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getResponseCode()).thenReturn(200);
        doReturn(connectionMock).when(spy).abrirConexaoViaCep(any());

        assertThrows(InvalidCepException.class, () -> spy.buscarEnderecoPorCep("01001000"));
    }

    @Test
    void deveLancarExcecaoQuandoFalhaNaLeitura() throws Exception {
        ViaCepService spy = Mockito.spy(viaCepService);
        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getInputStream()).thenThrow(new IOException("Erro na leitura"));
        when(connectionMock.getResponseCode()).thenReturn(200);

        doReturn(connectionMock).when(spy).abrirConexaoViaCep(any());

        assertThrows(InvalidCepException.class, () -> spy.buscarEnderecoPorCep("01001000"));
    }
}
