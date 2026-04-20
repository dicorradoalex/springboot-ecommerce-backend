package com.lipari.Academy2026.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Eccezione lanciata quando si tenta di eseguire un'operazione su un ordine
 * (es. cancellazione o aggiornamento stato) che non è consentita nello stato attuale.
 * Mappa lo stato HTTP 400 (BAD_REQUEST).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOrderStateException extends RuntimeException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
}
