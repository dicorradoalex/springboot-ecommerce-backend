package com.lipari.Academy2026.exception;

/**
 * Eccezione lanciata quando si tenta di eliminare una categoria che contiene ancora dei prodotti.
 * Serve per impedire la perdita accidentale di dati e garantire l'integrità referenziale.
 */
public class CategoryNotEmptyException extends RuntimeException {
    public CategoryNotEmptyException(String message) {
        super(message);
    }
}
