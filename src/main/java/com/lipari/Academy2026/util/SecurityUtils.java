package com.lipari.Academy2026.util;

import com.lipari.Academy2026.entity.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility per gestire le operazioni comuni legate alla sicurezza e al contesto utente.
 */
@Component
public class SecurityUtils {

    /**
     * Recupera l'utente attualmente autenticato dal contesto di sicurezza di Spring.
     */
    public UserEntity getCurrentUser() {
        return (UserEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }
}
