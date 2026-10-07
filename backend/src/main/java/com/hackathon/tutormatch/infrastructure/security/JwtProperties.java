package com.hackathon.tutormatch.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

/** Configuracion del JWT (jwt.*). */
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secret, Duration duracion, Duration duracionRecordar) {

    /** HS256 exige una clave de al menos 256 bits. */
    private static final int BYTES_MINIMOS = 32;

    public JwtProperties {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < BYTES_MINIMOS) {
            throw new IllegalStateException("jwt.secret (variable JWT_SECRET) debe tener al menos 32 caracteres");
        }
        if (duracion == null) {
            duracion = Duration.ofDays(1);
        }
        if (duracionRecordar == null) {
            duracionRecordar = Duration.ofDays(30);
        }
    }
}
