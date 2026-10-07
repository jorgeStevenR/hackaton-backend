package com.hackathon.tutormatch.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(example = "ada@correo.com")
        @NotBlank(message = "El correo es obligatorio")
        String email,

        @Schema(example = "Demo1234")
        @NotBlank(message = "La contraseña es obligatoria")
        String password,

        @Schema(example = "true", description = "Opcional: true da un token de mayor duración")
        Boolean rememberMe) {

    public LoginRequest {
        email = email == null ? null : email.trim();
    }
}
