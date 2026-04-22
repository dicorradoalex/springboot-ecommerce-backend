package com.lipari.Academy2026.service.stripe;

import com.lipari.Academy2026.entity.OrderEntity;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

/**
 * Servizio per gestire le interazioni con l'API di Stripe.
 */
public interface StripeService {

    /**
     * Crea una sessione di checkout Stripe per un dato ordine.
     * @param order L'entità ordine per cui creare il pagamento.
     * @return La sessione di checkout creata da Stripe.
     * @throws StripeException Se si verificano errori durante la chiamata alle API di Stripe.
     */
    Session createCheckoutSession(OrderEntity order) throws StripeException;
}
