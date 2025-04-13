package com.vitorsilvafranca.mobiauto_backend_integration_interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Endereco;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.NominatimService;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class NominatimServiceTest {

    @InjectMocks
    private NominatimService nominatimService;

    private final ObjectMapper mapper = new ObjectMapper();

    private Endereco endereco;

    private final String respostaValida = """
        [
            {
                "lat": "-23.550520",
                "lon": "-46.633308"
            }
        ]
        """;

    private final String respostaVazia = "[]";

    @BeforeEach
    void setUp() {
        endereco = new Endereco();
        endereco.setLogradouro("Praça da Sé");
        endereco.setCidade("São Paulo");
        endereco.setUf("SP");

        ReflectionTestUtils.setField(nominatimService, "mapper", mapper);
    }

    @Test
    void devePreencherLatitudeLongitudeComSucesso() throws Exception {
        // Arrange
        NominatimService spy = Mockito.spy(nominatimService);
        doReturn(respostaValida).when(spy).chamarNominatim(any());

        // Act
        spy.completarLatitudeLongitude(endereco);

        // Assert
        assertEquals(-23.550520, endereco.getLatitude(), 0.00001);
        assertEquals(-46.633308, endereco.getLongitude(), 0.00001);
    }

    @Test
    void deveLancarExcecaoQuandoRespostaVazia() throws Exception {
        // Arrange
        NominatimService spy = Mockito.spy(nominatimService);
        doReturn(respostaVazia).when(spy).chamarNominatim(any());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> spy.completarLatitudeLongitude(endereco));
    }

    @Test
    void deveLancarExcecaoQuandoJsonInvalido() throws Exception {
        // Arrange
        NominatimService spy = Mockito.spy(nominatimService);
        doReturn("{ json quebrado").when(spy).chamarNominatim(any());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> spy.completarLatitudeLongitude(endereco));
    }

    @Test
    void deveLancarExcecaoQuandoChamadaFalhar() throws Exception {
        // Arrange
        NominatimService spy = Mockito.spy(nominatimService);
        doThrow(new IOException("Erro")).when(spy).chamarNominatim(any());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> spy.completarLatitudeLongitude(endereco));
    }
}
