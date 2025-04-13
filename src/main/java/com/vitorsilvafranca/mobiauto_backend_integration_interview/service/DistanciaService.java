package com.vitorsilvafranca.mobiauto_backend_integration_interview.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Anunciante;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Endereco;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto.ResultadoRotaDTO;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.BadRequestException;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.LatitudeOrLongitudeInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

@Service
public class DistanciaService {

    private static final String OSRM_URL_TEMPLATE = "https://router.project-osrm.org/route/v1/driving/%.6f,%.6f;%.6f,%.6f?overview=false";

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private EnderecoService enderecoService;

    public ResultadoRotaDTO calcularDistancia(Endereco origem, Endereco destino) {
        validarCoordenadas(origem, destino);

        String url = construirUrl(origem, destino);

        String respostaJson = chamarOsrm(url);
        return extrairDistanciaETempo(respostaJson);
    }

    private void validarCoordenadas(Endereco origem, Endereco destino) {
        if (origem == null || destino == null) {
            throw new LatitudeOrLongitudeInvalidException("Endereços de origem e destino não podem ser nulos.");
        }

        Double latOrigem = origem.getLatitude();
        Double lonOrigem = origem.getLongitude();
        Double latDestino = destino.getLatitude();
        Double lonDestino = destino.getLongitude();

        if (latOrigem == null || lonOrigem == null || latDestino == null || lonDestino == null) {
            throw new LatitudeOrLongitudeInvalidException("Latitude e longitude não podem ser nulas.");
        }

        if (latOrigem == 0 || lonOrigem == 0 || latDestino == 0 || lonDestino == 0) {
            throw new LatitudeOrLongitudeInvalidException("Latitude e longitude não podem ser iguais a 0.");
        }
    }

    private String construirUrl(Endereco origem, Endereco destino) {
        return String.format(Locale.US, OSRM_URL_TEMPLATE,
                origem.getLongitude(), origem.getLatitude(),
                destino.getLongitude(), destino.getLatitude());
    }

    private String chamarOsrm(String urlStr) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String linha;
                while ((linha = in.readLine()) != null) {
                    response.append(linha);
                }
            }
            return response.toString();

        } catch (Exception e) {
            throw new BadRequestException("Erro ao consultar OSRM: " + e.getMessage());
        }
    }

    private ResultadoRotaDTO extrairDistanciaETempo(String jsonResponse) {
        try {
            JsonNode root = mapper.readTree(jsonResponse);

            if (!root.path("code").asText().equals("Ok")) {
                throw new BadRequestException("Resposta inválida da API OSRM.");
            }

            JsonNode rota = root.path("routes").get(0);
            double distanciaKm = rota.path("distance").asDouble() / 1000.0;
            double duracaoMin = rota.path("duration").asDouble() / 60.0;

            return new ResultadoRotaDTO(distanciaKm, duracaoMin);

        } catch (Exception e) {
            throw new BadRequestException("Erro ao processar resposta da OSRM: " + e.getMessage());
        }
    }

    public String calcularDistanciasFormatadas(Endereco origem, List<Anunciante> anunciantes) {
        StringBuilder resultadoFinal = new StringBuilder();

        for (Anunciante anunciante : anunciantes) {
            try {
                String cepDestino = anunciante.getEndereco().getCep().replace("-", "");
                Endereco destino = enderecoService.gravarEndereco(cepDestino);

                ResultadoRotaDTO rota = calcularDistancia(origem, destino);
                resultadoFinal.append(anunciante.getNome())
                        .append(" → ")
                        .append(rota.toString())
                        .append("\n");

            } catch (Exception e) {
                resultadoFinal.append("❌ Erro com lojista '")
                        .append(anunciante.getNome())
                        .append("': ")
                        .append(e.getMessage())
                        .append("\n");
            }
        }
        return resultadoFinal.toString().trim();
    }
}