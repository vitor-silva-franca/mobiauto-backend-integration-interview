package com.vitorsilvafranca.mobiauto_backend_integration_interview.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

@Service
public class OpenAiService {

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);;

    private ChatClient chatClient;

    public OpenAiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String escreverResposta(String message) {
        PromptTemplate promptTemplate = new PromptTemplate("Você é especialista em tráfego urbano, receberá uma lista de Lojistas, com suas respectivas distâncias em Km e tempo de distância em Minutos. Seu papel é organizar essa lista por ordem de proximidade e informar para o usuário os Lojistas mais próximos. Segue a lista: " + message);
        try {
            ChatResponse chatResponse = chatClient.prompt(promptTemplate.getTemplate()).call().chatResponse();

            String json = mapper.writeValueAsString(chatResponse);
            JsonNode root = mapper.readTree(json);
            String resposta = root.path("result").path("output").path("text").asText();
            return resposta;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair resposta do ChatGPT: " + e.getMessage());
        }
    }
}
