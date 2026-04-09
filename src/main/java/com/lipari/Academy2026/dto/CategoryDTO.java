package com.lipari.Academy2026.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record CategoryDTO(
        UUID id,

        @NotBlank(message = "Il nome è obbligatorio")
        String name) {
}

/*
    NOTE DIDATTICHE


    Annotazioni Validation
    - @NotBlank
      Garantisce che il nome della categoria non sia nullo, non sia vuoto e
      non contenga solo spazi.

    Utilizzo nelle API
    - In fase di Creazione (POST): Solitamente l'ID viene lasciato nullo perché
      generato dal database, mentre il 'name' è obbligatorio.

    - In fase di Risposta (GET): Vengono restituiti entrambi i campi per
      permettere al frontend di mostrare il nome e usare l'ID per filtri o modifiche.
-----------
*/