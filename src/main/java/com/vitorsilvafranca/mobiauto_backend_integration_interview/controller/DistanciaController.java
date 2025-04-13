package com.vitorsilvafranca.mobiauto_backend_integration_interview.controller;

import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Anunciante;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.Endereco;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto.ClienteDTO;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto.DistanciaParaAnuncianteDTO;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto.ResultadoRotaDTO;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/distancia")
public class DistanciaController {

    private final ClienteService clienteService;
    private final EnderecoService enderecoService;
    private final AnuncianteService anuncianteService;
    private final DistanciaService distanciaService;
    private final OpenAiService openAiService;

    public DistanciaController(ClienteService clienteService,
                               EnderecoService enderecoService,
                               AnuncianteService anuncianteService,
                               DistanciaService distanciaService,
                               OpenAiService openAiService) {
        this.clienteService = clienteService;
        this.enderecoService = enderecoService;
        this.anuncianteService = anuncianteService;
        this.distanciaService = distanciaService;
        this.openAiService = openAiService;
    }

    @Operation(
            summary = "(NÃO ORDENADO) Calcula distâncias de um cliente até todos os lojistas",
            description = "Calcula a distância entre um cliente (por ID ou CEP) até todos os lojistas cadastrados, retornando tempo e distância em km."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Distâncias calculadas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @GetMapping
    public ResponseEntity<List<DistanciaParaAnuncianteDTO>> calcularDistancia(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) String cep) {

        Endereco origem = determinarEnderecoOrigem(clienteId, cep);
        List<Anunciante> anunciantes = anuncianteService.buscarTodos();

        List<DistanciaParaAnuncianteDTO> resultados = new ArrayList<>();

        for (Anunciante anunciante : anunciantes) {
            try {
                String cepDestino = anunciante.getEndereco().getCep().replace("-", "");
                Endereco destino = enderecoService.gravarEndereco(cepDestino);

                ResultadoRotaDTO rota = distanciaService.calcularDistancia(origem, destino);

                DistanciaParaAnuncianteDTO dto = new DistanciaParaAnuncianteDTO(
                        anunciante.getId(),
                        anunciante.getNome(),
                        rota.getDistanciaKm(),
                        rota.getDuracaoMin()
                );

                resultados.add(dto);
            } catch (Exception e) {
                System.err.printf("Erro ao calcular distância para anunciante ID %d: %s%n", anunciante.getId(), e.getMessage());
            }
        }
        return ResponseEntity.ok(resultados);
    }

    private Endereco determinarEnderecoOrigem(Long clienteId, String cep) {
        if (clienteId != null) {
            ClienteDTO cliente = clienteService.buscarClienteDTO(clienteId);
            if (cliente.getCep() == null) {
                throw new IllegalArgumentException("Cliente não possui CEP cadastrado.");
            }
            return enderecoService.gravarEndereco(cliente.getCep().replace("-", ""));
        }
        if (cep != null && !cep.isBlank()) {
            return enderecoService.gravarEndereco(cep.replace("-", ""));
        }
        throw new IllegalArgumentException("Informe o ID do cliente ou um CEP válido.");
    }

    @Operation(
            summary = "(ORDENADO COM INTELIGÊNCIA ARTIFICIAL) Calcula distâncias de um cliente até todos os lojistas",
            description = "Calcula a distância entre um cliente (por ID ou CEP) até todos os lojistas cadastrados, retornando tempo e distância em km."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Distâncias calculadas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @GetMapping("/openai")
    public ResponseEntity<String> calcularDistanciasFormatadas(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) String cep) {

        Endereco origem = determinarEnderecoOrigem(clienteId, cep);
        List<Anunciante> anunciantes = anuncianteService.buscarTodos();

        String texto = distanciaService.calcularDistanciasFormatadas(origem, anunciantes);
        String resposta = openAiService.escreverResposta(texto);

        return ResponseEntity.ok(resposta);
    }
}