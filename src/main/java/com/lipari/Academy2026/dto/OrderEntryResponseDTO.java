package com.lipari.Academy2026.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderEntryResponseDTO(
        UUID id,

        @NotNull(message = "Il prodotto è obbligatorio")
        ProductDTO product,

        @NotNull(message = "La quantità è obbligatoria")
        @Positive(message = "La quantità deve essere maggiore di zero")
        Integer quantity,

        @NotNull(message = "Il prezzo è obbligatorio")
        @Positive(message = "Il prezzo deve essere maggiore di zero")
        BigDecimal price,

        @NotNull
        @Positive
        BigDecimal total
) {
}

/*
    NOTE DIDATTICHE

    - record
      Classe immutabile per trasportare i dati della singola riga di un ordine.

    Annotazioni Validation
    - @Positive
      Impedisce l'inserimento di valori negativi o zero.

    - @NotNull
      Controlla solo che l'oggetto non sia null.
      Accetta stringhe vuote "" o spazi " ". Si usa per numeri e oggetti.

    OMISSIONE DI OrderResponseDTO order

    - Se OrderResponseDTO contiene una List<OrderEntryResponseDTO> e OrderEntryResponseDTO contiene
      un OrderResponseDTO, il serializzatore JSON (Jackson) entrerà in un loop infinito
      provando a scriverli.
    - Soluzione: Nel DTO della riga d'ordine (Entry) si omette il riferimento
      all'ordine padre, poiché l'informazione è già implicita.

-----------
*/