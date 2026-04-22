package com.lipari.Academy2026.controller;

import com.lipari.Academy2026.service.order.OrderService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/webhook/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final OrderService orderService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;

        try {
            // Validazione della firma del webhook
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            log.error("Firma Webhook non valida: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Firma non valida");
        }

        // Gestione dell'evento
        String eventType = event.getType();
        log.info("Ricevuto evento Stripe: {}", eventType);

        try {
            // Avvolgiamo la deserializzazione in un try-catch per gestire l'eccezione di Java
            if ("checkout.session.completed".equals(eventType)) {
                Session session = (Session) event.getDataObjectDeserializer().deserializeUnsafe();
                if (session != null) {
                    log.info("Pagamento completato per la sessione: {}", session.getId());
                    orderService.handlePaymentSuccess(session.getId());
                } else {
                    log.error("Sessione null dopo la deserializzazione per l'evento {}", event.getId());
                }
            } else if ("checkout.session.async_payment_failed".equals(eventType)) {
                Session session = (Session) event.getDataObjectDeserializer().deserializeUnsafe();
                if (session != null) {
                    log.warn("Pagamento fallito per la sessione: {}", session.getId());
                    orderService.handlePaymentFailure(session.getId());
                } else {
                    log.error("Sessione null dopo la deserializzazione per l'evento di fallimento {}", event.getId());
                }
            }
        } catch (EventDataObjectDeserializationException e) {
            // Se la deserializzazione forzata fallisce drasticamente, lo intercettiamo qui
            log.error("Errore critico durante la deserializzazione dell'evento Stripe: {}", e.getMessage());
        }

        return ResponseEntity.ok("Evento ricevuto");
    }
}