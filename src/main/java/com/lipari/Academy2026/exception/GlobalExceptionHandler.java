package com.lipari.Academy2026.exception;

import com.lipari.Academy2026.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 *  Questa classe è il Gestore Globale degli Errori. Permette di gestire le eccezioni
 *  di tutti i @RestController in un unico posto. Lo fa "intercettando" le eccezioni
 *  lanciate dai controller (e dai service sottostanti) e trasformandole in un formato
 *  JSON standard (ErrorResponseDTO)
 */

// @RestControllerAdvice permette di monitorare tutti i @RestController
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Intercetta specificatamente le ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(), // Estrai il codice dell'errore (404)
                ex.getMessage(), // Estrai il messaggio scritto nel Service
                LocalDateTime.now() // Cattura l'orario
        );
        // Restituisci l'oggetto (che diventerà JSON) e specifica lo stato HTTP 404
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
