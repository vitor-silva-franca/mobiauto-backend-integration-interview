package com.vitorsilvafranca.mobiauto_backend_integration_interview.controller.handler;

import com.vitorsilvafranca.mobiauto_backend_integration_interview.model.dto.ErrorResponseDTO;
import com.vitorsilvafranca.mobiauto_backend_integration_interview.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(EntityNotCreatedException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotCreated(EntityNotCreatedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(CepNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCepNotFoundException(CepNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(LatitudeOrLongitudeInvalidException.class)
    public ResponseEntity<ErrorResponseDTO> handleLatitudeOrLongitudeInvalidException(LatitudeOrLongitudeInvalidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(InvalidCepException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCepException(InvalidCepException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .reduce((msg1, msg2) -> msg1 + " | " + msg2)
                .orElse("Requisição inválida.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO(mensagem));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Erro: " + ex.getMessage()));
    }
}
