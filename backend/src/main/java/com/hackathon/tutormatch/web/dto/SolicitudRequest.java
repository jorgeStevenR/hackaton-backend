package com.hackathon.tutormatch.web.dto;

import com.hackathon.tutormatch.domain.model.Modalidad;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SolicitudRequest(
        @Schema(example = "Pedro Perez")
        @NotBlank(message = "El nombre del estudiante es obligatorio")
        String nombreEstudiante,

        @Schema(example = "Calculo")
        @NotBlank(message = "La materia es obligatoria")
        String materia,

        @Schema(example = "[\"LUN-08\", \"MIE-10\", \"VIE-14\"]")
        @NotEmpty(message = "Debe indicar al menos un horario")
        List<String> horarios,

        @Schema(example = "VIRTUAL")
        @NotNull(message = "La modalidad es obligatoria (PRESENCIAL, VIRTUAL o AMBAS)")
        Modalidad modalidad) {
}
