package com.lipari.Academy2026.dto.order;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO per il dettaglio di una riga d'ordine in risposta.
 * Utilizza dati "storicizzati" (productId e productName) per garantire la leggibilità
 * anche se il prodotto originale viene rimosso dal catalogo.
 */
public record OrderEntryResponseDTO(
        UUID id,
        UUID productId,
        String productName,
        Integer quantity,
        BigDecimal price,
        BigDecimal total
) {}
