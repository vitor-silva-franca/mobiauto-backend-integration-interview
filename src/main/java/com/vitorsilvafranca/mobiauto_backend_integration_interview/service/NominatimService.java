package com.vitorsilvafranca.mobiauto_backend_integration_interview.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Endereco;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.BadRequestException;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class NominatimService {

    private static final String NOMINATIM_URL_TEMPLATE = "https://nominatim.openstreetmap.org/search?q=%s&format=json&limit=1";

    private final ObjectMapper mapper = new ObjectMapper();

    public void completarLatitudeLongitude(Endereco endereco) {
        try {
            String enderecoCompleto = montarEndereco(endereco);

            String respostaJson = chamarNominatim(enderecoCompleto);
            preencherLatitudeLongitude(endereco, respostaJson);

        } catch (Exception e) {
            throw new BadRequestException("Não foi possível obter coordenadas geográficas do endereço.");
        }
    }

    private String montarEndereco(Endereco e) {
        return String.format("%s, %s, %s, Brasil", e.getLogradouro(), e.getCidade(), e.getUf());
    }

    public String chamarNominatim(String enderecoCompleto) throws Exception {
        String encoded = URLEncoder.encode(enderecoCompleto, "UTF-8");
        String url = String.format(NOMINATIM_URL_TEMPLATE, encoded);

        HttpURLConnection connection = abrirConexao(url);
        return lerResposta(connection);
    }

    private HttpURLConnection abrirConexao(String urlStr) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        return conn;
    }

    private String lerResposta(HttpURLConnection connection) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder resposta = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                resposta.append(linha);
            }
            return resposta.toString();
        } catch (Exception e) {
            throw new BadRequestException("Erro ao ler resposta da API Nominatim: " + e.getMessage());
        } finally {
            connection.disconnect();
        }
    }

    private void preencherLatitudeLongitude(Endereco endereco, String json) {
        try {
            JsonNode root = mapper.readTree(json);

            if (!root.isArray() || root.isEmpty()) {
                throw new BadRequestException("Latitude e longitude não encontradas para o endereço.");
            }

            JsonNode location = root.get(0);
            endereco.setLatitude(location.path("lat").asDouble());
            endereco.setLongitude(location.path("lon").asDouble());

        } catch (Exception e) {
            throw new BadRequestException("Erro ao processar JSON da Nominatim: " + e.getMessage());
        }
    }
}