package com.hackathon.tutormatch.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistroRequest(
        @Schema(example = "Ada Lovelace")
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
        String name,

        @Schema(example = "ada@correo.com")
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato válido")
        String email,

        @Schema(example = "Demo1234")
        @NotBlank(message = "La contraseña es obligatoria")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
                message = "La contraseña debe tener mínimo 8 caracteres, con al menos una mayúscula, una minúscula y un número")
        String password) {

    /** Quita espacios alrededor antes de validar ("  ada@correo.com " es valido). */
    public RegistroRequest {
        name = name == null ? null : name.trim();
        email = email == null ? null : email.trim();
    }
}
