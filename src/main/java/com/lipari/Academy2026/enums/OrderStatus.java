package com.lipari.Academy2026.enums;

/**
 * Rappresenta i possibili stati di un ordine nel sistema.
 */
public enum OrderStatus {
    /** Ordine creato, in attesa del completamento del pagamento su Stripe. */
    PENDING_PAYMENT,
    
    /** Pagamento confermato con successo. */
    PAID,
    
    /** Il pagamento è fallito o è stato rifiutato. */
    PAYMENT_FAILED,
    
    /** L'ordine è stato spedito. */
    SHIPPED,
    
    /** L'ordine è stato consegnato al cliente. */
    DELIVERED,
    
    /** L'ordine è stato annullato (dal cliente o dall'admin). */
    CANCELED
}
