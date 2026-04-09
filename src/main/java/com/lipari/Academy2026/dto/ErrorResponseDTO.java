package com.lipari.Academy2026.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        int status,
        String message,
        LocalDateTime timestamp
) { }


/*
    NOTE DIDATTICHE - ErrorResponseDTO

    - record
      Classe immutabile usata per standardizzare la risposta in caso di errore.
      Evita che il server restituisca "muri di testo" o stacktrace poco leggibili.

    Campi
    - int status:
      Il codice numerico dell'errore HTTP (es: 400 per dati non validi, 404 per non trovato).

    - String message:
      Una descrizione chiara dell'errore (es: "Il formato email non è valido").

    - LocalDateTime timestamp:
      Il momento esatto in cui si è verificato l'errore. Utile per il debugging e i log.

    Utilizzo (Global Exception Handling)
    - Questo DTO viene usato insieme a un @RestControllerAdvice.
    - Quando scatta un'eccezione (es. fallisce la validazione di un @Valid),
      Spring cattura l'errore e "riempie" questo DTO per inviarlo al client.

-----------
*/