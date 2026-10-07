package com.hackathon.tutormatch.infrastructure.config;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/** Pesos de cada criterio (matching.pesos.*). Deben sumar 1.0. */
@Validated
@ConfigurationProperties(prefix = "matching.pesos")
public record MatchingProperties(
        @DecimalMin("0.0") @DecimalMax("1.0") double horario,
        @DecimalMin("0.0") @DecimalMax("1.0") double nivel,
        @DecimalMin("0.0") @DecimalMax("1.0") double calificacion,
        @DecimalMin("0.0") @DecimalMax("1.0") double modalidad) {

    private static final double TOLERANCIA = 0.0001;

    public MatchingProperties {
        double suma = horario + nivel + calificacion + modalidad;
        if (Math.abs(suma - 1.0) > TOLERANCIA) {
            throw new IllegalStateException(
                    "Los pesos de matching.pesos deben sumar 1.0 y suman " + suma);
        }
    }
}
