package com.vitorsilvafranca.mobiauto_backend_integration_interview.controller;

import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto.AnuncianteDTO;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.AnuncianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/anunciante")
public class AnuncianteController {

    private final AnuncianteService anuncianteService;

    public AnuncianteController(AnuncianteService anuncianteService) {
        this.anuncianteService = anuncianteService;
    }

    @Operation(
            summary = "Cadastra um novo lojista",
            description = "(REMOVA O CAMPO DE ID) Cria um novo anunciante a partir de um nome e CEP. O endereço será completado via integração com o ViaCEP e Nominatim."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lojista criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<?> salvarAnunciante(@Valid @RequestBody AnuncianteDTO anuncianteDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(anuncianteService.salvarAnunciante(anuncianteDTO));
    }
}
