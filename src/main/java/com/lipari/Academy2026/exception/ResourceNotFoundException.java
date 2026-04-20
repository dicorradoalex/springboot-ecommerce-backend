package com.lipari.Academy2026.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Eccezione lanciata quando una risorsa richiesta tramite ID o nome non viene trovata nel database.
 * Mappa automaticamente lo stato HTTP 404 (NOT_FOUND).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
