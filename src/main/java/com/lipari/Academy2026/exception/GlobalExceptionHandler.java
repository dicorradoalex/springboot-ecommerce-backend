package com.lipari.Academy2026.exception;

import com.lipari.Academy2026.dto.error.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Gestore Globale degli Errori per l'applicazione.
 * Intercetta le eccezioni lanciate dai Controller e le trasforma in ErrorResponseDTO standardizzati.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ECCEZIONI DI BUSINESS (CUSTOM)

    /**
     * Gestisce il fallimento dell'ordine per mancanza di disponibilità in magazzino.
     */
    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ErrorResponseDTO> handleOutOfStock(OutOfStockException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gestisce i casi in cui una risorsa richiesta non è presente nel sistema (404).
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Gestisce il tentativo di eliminazione di una categoria che non è vuota.
     */
    @ExceptionHandler(CategoryNotEmptyException.class)
    public ResponseEntity<ErrorResponseDTO> handleCategoryNotEmpty(CategoryNotEmptyException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gestisce i conflitti durante la creazione di risorse duplicate (409).
     */
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleAlreadyExistsException(AlreadyExistsException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Gestisce transizioni di stato non valide per gli ordini (400).
     */
    @ExceptionHandler(InvalidOrderStateException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidOrderState(InvalidOrderStateException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    // ECCEZIONI DI FRAMEWORK (SPRING / SECURITY)

    /**
     * Gestisce il fallimento dell'autenticazione per credenziali errate (401).
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(BadCredentialsException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Credenziali non valide: email o password errate.",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Gestisce gli errori di validazione dei DTO (es. @NotBlank, @Positive).
     * Colleziona tutti i messaggi d'errore in un'unica stringa descrittiva.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Errore di validazione: " + errorMessage,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}

/*
    NOTE DIDATTICHE

    2. Eccezioni Custom vs Framework:
       - Custom: Create da noi per logiche di business (es. ResourceNotFoundException).
       - Framework: Lanciate automaticamente da Spring o Security (es. BadCredentialsException).

    3. Standardizzazione (ErrorResponseDTO):
       Qualsiasi errore viene trasformato in un JSON coerente, facilitando il lavoro del Frontend.
-----------
*/
