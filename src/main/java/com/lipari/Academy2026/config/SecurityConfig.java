package com.lipari.Academy2026.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity // Abilita la sicurezza web personalizzata in Spring Boot
public class SecurityConfig {

    /**
     * Definisco il sistema di criptazione delle password (Hashing).
     * Usiamo BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configurazione della sicurezza (regole di accesso)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disabilita CSRF (API REST stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // Regole di autorizzazione (chi può accedere a cosa)
                .authorizeHttpRequests(auth -> auth
                        // Endpoint pubblici (login / register)
                        .requestMatchers("/api/auth/**").permitAll()

                        // Endpoint pubblici di lettura
                        .requestMatchers("/api/product/**").permitAll()
                        .requestMatchers("/api/category/**").permitAll()

                        // Tutto il resto richiede autenticazione
                        .anyRequest().authenticated()
                )

                // Autenticazione Basic (utile per test)
                .httpBasic(withDefaults());

        return http.build();
    }
}


/*
    NOTE DIDATTICHE

    - @Configuration
      Indica a Spring che quella classe è una classe di configurazione,
      cioè una classe il cui scopo è dichiarare bean

    - @Bean
      Indica a Spring di prendere il valore restituito dal metodo e di registrarlo come bean nel contesto,
      in modo che si possa iniettare ovunque serva.

    - PasswordEncoder
      È un'interfaccia: chi la implementa deve avere un metodo per criptare le password e uno per confrontarle

    - ByCryptPasswordEncoder
      È il password encoder di ByCrypt che andremo ad utilizzare


 */