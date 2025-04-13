package com.vitorsilvafranca.mobiauto_backend_integration_interview.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Endereco;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.BadRequestException;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.CepNotFoundException;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.InvalidCepException;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ViaCepService {

    private static final String VIACEP_URL = "https://viacep.com.br/ws/%s/json/";
    private static final String ACCEPT_HEADER = "application/json";

    private final ObjectMapper mapper = new ObjectMapper();

    public Endereco buscarEnderecoPorCep(String cep) {
        try {
            String url = construirUrlViaCep(cep);
            HttpURLConnection conexao = abrirConexaoViaCep(url);
            validarStatusHttp(conexao);

            String respostaJson = lerResposta(conexao);
            JsonNode json = parseJson(respostaJson);

            validarConteudoViaCep(json);
            Endereco endereco = mapearEndereco(json);

            return endereco;

        } catch (Exception e) {
            throw new InvalidCepException("CEP inválido. Informe um válido.");
        }
    }

    private String construirUrlViaCep(String cep) {
        return String.format(VIACEP_URL, cep);
    }

    public HttpURLConnection abrirConexaoViaCep(String url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", ACCEPT_HEADER);
        return connection;
    }

    private void validarStatusHttp(HttpURLConnection connection) throws Exception {
        int status = connection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new CepNotFoundException("Erro HTTP ao consultar CEP. Código: " + status);
        }
    }

    public String lerResposta(HttpURLConnection connection) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder resposta = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                resposta.append(linha);
            }
            return resposta.toString();
        } catch (Exception e) {
            throw new BadRequestException("Erro ao ler resposta da API ViaCEP: " + e.getMessage());
        } finally {
            connection.disconnect();
        }
    }

    private JsonNode parseJson(String json) {
        try {
            return mapper.readTree(json);
        } catch (Exception e) {
            throw new BadRequestException("Erro ao processar JSON do ViaCEP: " + e.getMessage());
        }
    }

    private void validarConteudoViaCep(JsonNode node) {
        if (node.has("erro") && node.get("erro").asBoolean()) {
            throw new CepNotFoundException("CEP inválido ou inexistente.");
        }
    }

    private Endereco mapearEndereco(JsonNode node) {
        Endereco endereco = new Endereco();
        endereco.setCep(node.path("cep").asText(""));
        endereco.setLogradouro(node.path("logradouro").asText(""));
        endereco.setBairro(node.path("bairro").asText(""));
        endereco.setCidade(node.path("localidade").asText(""));
        endereco.setUf(node.path("uf").asText(""));
        return endereco;
    }
}