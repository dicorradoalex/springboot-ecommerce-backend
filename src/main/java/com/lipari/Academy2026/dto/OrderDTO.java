package com.lipari.Academy2026.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(
        UUID id,

        @NotNull(message = "L'utente è obbligatorio")
        UserDTO user,

        @NotBlank(message = "Lo stato dell'ordine è obbligatorio")
        String status,

        @NotNull(message = "La data dell'ordine è obbligatoria")
        LocalDateTime orderTime,

        @NotEmpty(message = "L'ordine deve contenere almeno un prodotto")
        @Valid
        List<OrderEntryDTO> entries) {
}

/*
    NOTE DIDATTICHE

    Annotazioni Validation
    - @NotEmpty (sulle Liste)
      A differenza di @NotBlank, si usa per le collezioni. Garantisce che la lista
      non sia nulla e che contenga almeno un elemento (un ordine senza prodotti non ha senso).

    - @Valid (Validazione a Cascata)
      Valida ogni singolo 'OrderEntryDTO' dentro la lista.

    Gestione della Circolarità (JSON)
    - Poiché OrderDTO contiene una lista di OrderEntryDTO, è obbligatorio che
      OrderEntryDTO NON contenga a sua volta un OrderDTO.
      In caso contrario, la serializzazione JSON entrerebbe in loop (StackOverflow).
-----------

*/