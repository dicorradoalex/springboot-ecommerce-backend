package com.lipari.Academy2026.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity // Abilita la sicurezza web personalizzata in Spring Boot
@RequiredArgsConstructor

public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    /**
     * Sistema di criptazione delle password (Hashing).
     * Usiamo BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationProvider: collega UserDetailsService + PasswordEncoder
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * AuthenticationManager: usato per login (email/password)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }



    /**
     * Configurazione della sicurezza (regole di accesso)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                // Stateless (JWT -> niente sessioni)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Provider custom
                .authenticationProvider(authenticationProvider())

                // Regole accesso
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/product/**").permitAll()
                        .requestMatchers("/api/category/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // Area Amministrativa: accessibile solo con ruolo ADMIN
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                // JWT filter prima dell’autenticazione standard Spring
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

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

    - SessionCreationPolicy.STATELESS
     Il server "dimentica" l'utente non appena la richiesta è finita.
     L'utente deve mostrare il JWT ad ogni nuova chiamata.

    - AuthenticationManager
      Utilizzato nel AuthService per dire a spring di controllare se le
      credenziali inserite sono giuste.


 */