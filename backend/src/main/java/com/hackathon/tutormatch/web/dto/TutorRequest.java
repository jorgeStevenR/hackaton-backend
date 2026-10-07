package com.hackathon.tutormatch.web.dto;

import com.hackathon.tutormatch.domain.model.Modalidad;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TutorRequest(
        @Schema(example = "Ana Martinez")
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @Schema(example = "Calculo,Fisica")
        @NotBlank(message = "Debe indicar al menos una materia")
        String materias,

        @Schema(example = "LUN-08,MIE-10")
        @NotBlank(message = "Debe indicar al menos un horario")
        String horarios,

        @Schema(example = "3", description = "1 = basico, 2 = intermedio, 3 = experto")
        @NotNull(message = "El nivel es obligatorio")
        @Min(value = 1, message = "El nivel minimo es 1")
        @Max(value = 3, message = "El nivel maximo es 3")
        Integer nivel,

        @Schema(example = "4.8")
        @NotNull(message = "La calificacion es obligatoria")
        @DecimalMin(value = "1.0", message = "La calificacion minima es 1")
        @DecimalMax(value = "5.0", message = "La calificacion maxima es 5")
        Double calificacion,

        @Schema(example = "AMBAS")
        @NotNull(message = "La modalidad es obligatoria (PRESENCIAL, VIRTUAL o AMBAS)")
        Modalidad modalidad) {
}
