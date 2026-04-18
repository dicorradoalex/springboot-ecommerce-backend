package com.lipari.Academy2026.exception;

import com.lipari.Academy2026.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 *  Questa classe è il Gestore Globale degli Errori. Permette di gestire le eccezioni
 *  di tutti i @RestController in un unico posto. Lo fa "intercettando" le eccezioni
 *  lanciate dai controller (e dai service sottostanti) e trasformandole in un formato
 *  JSON standard (ErrorResponseDTO)
 */

// @RestControllerAdvice permette di monitorare tutti i @RestController
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Gestisce gli errori di validazione (es. @NotBlank, @Size, @Email falliti)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Recuperiamo tutti i messaggi d'errore e li uniamo in una stringa
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Errore di validazione: " + errorMessage,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

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

    // Intercetta specificatamente le AlreadyExistsException
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleAlreadyExistsException(AlreadyExistsException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(), // Estrai il codice dell'errore (409)
                ex.getMessage(), // Estrai il messaggio scritto nel Service
                LocalDateTime.now() // Cattura l'orario
        );
        // Restituisci l'oggetto (che diventerà JSON) e specifica lo stato HTTP 409
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // Intercetta specificatamente le AlreadyExistsException
    @ExceptionHandler(InvalidOrderStateException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidOrderState(InvalidOrderStateException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(), // Estrai il codice dell'errore (400,)
                ex.getMessage(), // Estrai il messaggio scritto nel Service
                LocalDateTime.now() // Cattura l'orario
        );
        // Restituisci l'oggetto (che diventerà JSON) e specifica lo stato HTTP 400,
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}

/*
    NOTE DIDATTICHE - [GlobalExceptionHandler]

    1. Eccezioni di Business vs Eccezioni di Framework
       - Eccezioni di Business (Personalizzate): Sono classi create da noi (es. ResourceNotFoundException).
         Le lanciamo "a mano" nei Service quando una regola del nostro ecommerce fallisce.
       - Eccezioni di Framework (Native): Sono lanciate automaticamente da Spring (es. MethodArgumentNotValidException).
         Vengono generate quando i vincoli tecnici (come @Size o @Email nei DTO) non sono rispettati.

    2. Perché non creare una "ValidationException" personalizzata?
       Spring ha già un'eccezione nativa perfetta per la validazione. Invece di crearne una nuova
       (che ci costringerebbe a fare un "travaso" di dati inutile), istruiamo il gestore globale
       a intercettare direttamente quella di sistema. Questo rende il codice più pulito e performante.

    3. Il Ruolo del "Collettore Universale"
       Il GlobalExceptionHandler agisce come una rete da pesca: cattura sia le nostre eccezioni
       custom che quelle tecniche di Spring, trasformandole tutte in un formato JSON standard (ErrorResponseDTO).
       In questo modo, chi usa le nostre API riceve sempre errori con la stessa struttura,
       indipendentemente da chi ha lanciato l'allarme.
-----------
*/

