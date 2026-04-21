package com.lipari.Academy2026.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Servizio per la gestione dei JSON Web Token (JWT).
 * Si occupa di generazione, estrazione e validazione dei token.
 */
@Service
public class JwtService {

    /**
     * Chiave segreta per firmare i token.
     * In produzione deve essere esterna alla codebase (in file di properties).
     */
    @Value("${jwt.secret.key}")
    private String secretKey; // Generata con power shell

    @Value("${jwt.expiration.ms}")
    private long jwtExpiration;


    /**
     * Estrae lo username (email) dal token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Genera un token JWT per un utente.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token JWT con eventuali claim extra.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Verifica se il token è valido per l'utente.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}


/*
    NOTE DIDATTICHE

    - Claims:
      Sono le informazioni contenute nel token (subject, expiration,
      e dati personalizzati come ruoli).

    - Signature:
      È la firma digitale del token.
      Viene generata usando header + payload + SECRET_KEY.
      Se il token viene modificato, la firma non corrisponde più
      e il server lo rifiuta.

    - Base64:
      La chiave segreta viene codificata in Base64 per poter essere
      gestita come stringa.

    - Scadenza:
      In sistemi reali si usano durate molto più brevi (15–60 min).
*/