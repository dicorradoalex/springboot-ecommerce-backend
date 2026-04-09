package com.lipari.Academy2026.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;
import java.util.List;

public record UserDTO(
        UUID id,

        @NotBlank
        @Email(message = "Formato email non valido")
        String email,

        @NotBlank(message = "Il nome è obbligatorio")
        String name,

        @NotBlank(message = "Il cognome è obbligatorio")
        String surname,

        @NotBlank(message = "L'indirizzo è obbligatorio")
        @Size(max = 200)
        String address,

        @NotBlank(message = "La città è obbligatoria")
        @Size(max = 100)
        String city,

        @NotBlank(message = "La nazione è obbligatoria")
        @Size(max = 50)
        String country,

        @Valid
        List<OrderDTO> orders) {
}

/*
    NOTE DIDATTICHE

    - record
      Classe immutabile. Genera automaticamente costruttore,
      getter, equals, hashCode e toString.

    Annotazioni Validation
    - @Email
      Controlla che la stringa inserita rispetti il formato standard di un indirizzo
      email (presenza di @, dominio, ecc.). Se il formato è errato, restituisce
      il messaggio personalizzato.

    - @NotBlank
      Assicura che la stringa non sia null e non contenga solo spazi.

    - @Size(min = X, max = Y)
      Limita il numero di caratteri. È un'ottima pratica per far coincidere
      il DTO con i limiti definiti nell'Entity (@Column(length = ...)).


    - @Valid (su Liste/Oggetti annidati)
      È fondamentale per la "Validazione Ricorsiva".
      Esempio: Se invio un utente con una lista di ordini, @Valid dice a Spring
      di controllare anche ogni singolo OrderDTO dentro la lista. Senza di questo,
      gli ordini verrebbero accettati anche se contengono dati errati.

    - @NotEmpty (per le Liste)
      Se volessimo obbligare l'utente ad avere almeno un ordine nella lista,
      useremmo @NotEmpty. (Nota: @NotBlank non si può usare sulle liste!).
-----------

*/
