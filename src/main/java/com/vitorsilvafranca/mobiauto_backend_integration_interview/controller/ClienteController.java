package com.vitorsilvafranca.mobiauto_backend_integration_interview.controller;

import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto.ClienteDTO;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(
            summary = "Cadastra um novo cliente",
            description = "(REMOVA O CAMPO DE ID) Cria um novo cliente a partir de um nome e CEP. O endereço será completado via integração com o ViaCEP e Nominatim."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<ClienteDTO> salvarCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.salvarCliente(clienteDTO));
    }
}