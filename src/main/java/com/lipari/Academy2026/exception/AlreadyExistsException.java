package com.lipari.Academy2026.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Eccezione lanciata quando si tenta di creare una risorsa (es. Utente o Categoria)
 * che è già presente nel database con lo stesso identificativo unico.
 * Mappa automaticamente lo stato HTTP 409 (CONFLICT).
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}