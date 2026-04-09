package com.lipari.Academy2026.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Record per il trasferimento dei dati del Prodotto.
 */
public record ProductDTO(
        UUID id,

        @NotBlank(message = "Il nome è obbligatorio")
        String name,

        @NotNull(message = "Il prezzo è obbligatorio")
        @Positive(message = "Il prezzo deve essere maggiore di zero")
        BigDecimal price,

        String description,

        @NotNull(message = "La categoria è obbligatoria")
        CategoryDTO category) {
}

/*
    NOTE DIDATTICHE

    - record java
      Classe immutabile introdotta in Java 14. I campi sono 'final' e non possono cambiare.
      Ideale per i DTO perché trasportano dati senza logica di business.


    Concetto di Validation (Jakarta Bean Validation)

    - Cosa sono
      Permettono di verificare che i dati dell'utente siano corretti prima
      di elaborarli. Se i requisiti non sono rispettati, la richiesta viene bloccata
      dal framework e il codice del Controller non viene nemmeno eseguito.

    - Come si usano
      1. Si dichiarano le regole nel DTO (es. @NotBlank, @Positive).
      2. Si attivano nel Controller con l'annotazione @Valid (es. @Valid @RequestBody DTO dto).


    Approfondimento Vincoli (Constraint)
    - @NotNull
      Il più generico. Controlla solo che l'oggetto non sia null.
      Accetta stringhe vuote "" o spazi " ". Si usa per numeri e oggetti.

    - @NotEmpty
      Controlla che non sia null e che la lunghezza sia > 0.
      Accetta stringhe con soli spazi " ". Si usa per Liste e Collezioni.

    - @NotBlank
      Il più severo. Controlla che non sia null, non sia vuoto e non
      contenga solo spazi. Si usa solo per le Stringhe (Nomi, Email, etc.).

    - @Positive: Garantisce che un numero (BigDecimal, Integer) sia > 0.
      Usare sempre per prezzi e quantità.
-----------
*/