package com.lipari.Academy2026.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Eccezione lanciata quando la quantità richiesta di un prodotto
 * supera la disponibilità effettiva in magazzino.
 * Mappa lo stato HTTP 400 (BAD_REQUEST).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String message) {
        super(message);
    }
}
