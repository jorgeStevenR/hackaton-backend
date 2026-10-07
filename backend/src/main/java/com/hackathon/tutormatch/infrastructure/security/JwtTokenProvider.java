package com.hackathon.tutormatch.infrastructure.security;

import com.hackathon.tutormatch.domain.model.Usuario;
import com.hackathon.tutormatch.domain.port.TokenProviderPort;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

/** JWT firmado con HS256, sin estado: el servidor no guarda sesiones. */
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class JwtTokenProvider implements TokenProviderPort {

    private final SecretKey clave;
    private final JwtProperties propiedades;

    public JwtTokenProvider(JwtProperties propiedades) {
        this.propiedades = propiedades;
        this.clave = Keys.hmacShaKeyFor(propiedades.secret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generar(Usuario usuario, boolean recordarSesion) {
        Duration duracion = recordarSesion ? propiedades.duracionRecordar() : propiedades.duracion();
        Instant ahora = Instant.now();
        return Jwts.builder()
                .subject(String.valueOf(usuario.getId()))
                .claim("email", usuario.getEmail())
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plus(duracion)))
                .signWith(clave)
                .compact();
    }

    @Override
    public Optional<Long> validar(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        try {
            String subject = Jwts.parser().verifyWith(clave).build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            return Optional.of(Long.valueOf(subject));
        } catch (JwtException | IllegalArgumentException e) {
            // Firma invalida, token vencido o mal formado
            return Optional.empty();
        }
    }
}
