package com.lipari.Academy2026.mapper;

import com.lipari.Academy2026.dto.cart.CartItemResponseDTO;
import com.lipari.Academy2026.dto.cart.CartResponseDTO;
import com.lipari.Academy2026.entity.CartEntity;
import com.lipari.Academy2026.entity.CartItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

/**
 * Mapper per la conversione tra entità Carrello e i relativi DTO.
 * Gestisce il calcolo dinamico di subtotali e totale complessivo.
 */
@Mapper(componentModel = "spring")
public interface CartMapper {

    /**
     * Converte un'entità Carrello in un DTO di risposta completo.
     */
    @Mapping(target = "total", expression = "java(calculateTotal(entity))")
    CartResponseDTO toDto(CartEntity entity);

    /**
     * Converte una riga del carrello in un DTO di dettaglio.
     */
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "unitPrice", source = "product.price")
    @Mapping(target = "subtotal", expression = "java(calculateSubtotal(item))")
    CartItemResponseDTO toItemDto(CartItemEntity item);

    /**
     * Calcola il costo totale di una riga (quantità * prezzo unitario).
     */
    default BigDecimal calculateSubtotal(CartItemEntity item) {
        if (item.getProduct() == null || item.getProduct().getPrice() == null) {
            return BigDecimal.ZERO;
        }
        // Prende il prezzo dal prodotto e lo moltiplica per la quantità
        return item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    /**
     * Calcola il costo totale dell'intero carrello sommando i subtotali (Metodo Default).
     */
    default BigDecimal calculateTotal(CartEntity entity) {
        if (entity.getItems() == null)
            return BigDecimal.ZERO;
        // Prendi la lista di righe, calcola il subtotale di ognuna e sommale tutte
        return entity.getItems().stream()
                .map(this::calculateSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

/*
    NOTE DIDATTICHE - [CartMapper]

    expression:
       Posso usare 'expression' in @Mapping per invocare i metodi default.
       Cosi posso inserire dei dati nel DTO che non esistono nel DB.

    Metodi Default:
       In un'interfaccia MapStruct, i metodi 'default' permettono di scrivere
       logica personalizzata che il mapper userà durante la conversione.

    Integrità:
       Si recupera sempre il prezzo dal prodotto (unitPrice) per garantire
       che l'utente veda il costo sempre aggiornato.



    @Mapping(target = "total", expression = "java(calculateTotal(entity))")

    stai dicendo:
                    "Per riempire il campo total nel DTO di risposta, invoca il mio
                     metodo Java calculateTotal passandogli l'oggetto entity che hai
                     appena ricevuto"
-----------
*/
