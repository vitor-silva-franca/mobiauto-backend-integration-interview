package com.vitorsilvafranca.mobiauto_backend_integration_interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Endereco;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto.ResultadoRotaDTO;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.DistanciaService;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.EnderecoService;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.BadRequestException;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.LatitudeOrLongitudeInvalidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DistanciaServiceTest {

    @InjectMocks
    private DistanciaService distanciaService;

    @Mock
    private EnderecoService enderecoService;

    private final ObjectMapper mapper = new ObjectMapper();

    private Endereco origem;
    private Endereco destino;

    @BeforeEach
    void setUp() {
        origem = new Endereco();
        origem.setLatitude(-23.561684);
        origem.setLongitude(-46.625378);

        destino = new Endereco();
        destino.setLatitude(-23.564000);
        destino.setLongitude(-46.655000);

        ReflectionTestUtils.setField(distanciaService, "mapper", mapper);
    }

    @Test
    void deveCalcularDistanciaComSucesso() {
        String json = """
            {
              "code": "Ok",
              "routes": [{
                "distance": 1800.0,
                "duration": 600.0
              }]
            }
        """;

        ResultadoRotaDTO resultado = ReflectionTestUtils.invokeMethod(distanciaService, "extrairDistanciaETempo", json);

        assertEquals(1.8, resultado.getDistanciaKm(), 0.01);
        assertEquals(10.0, resultado.getDuracaoMin(), 0.01);
    }

    @Test
    void deveLancarExcecaoParaRespostaInvalidaDaOsrm() {
        String jsonInvalido = """
            {
              "code": "Error",
              "routes": []
            }
        """;

        assertThrows(BadRequestException.class, () -> {
            ReflectionTestUtils.invokeMethod(distanciaService, "extrairDistanciaETempo", jsonInvalido);
        });
    }

    @Test
    void deveLancarExcecaoParaCoordenadasInvalidas() {
        origem.setLatitude(null);
        assertThrows(LatitudeOrLongitudeInvalidException.class,
                () -> distanciaService.calcularDistancia(origem, destino));
    }
}