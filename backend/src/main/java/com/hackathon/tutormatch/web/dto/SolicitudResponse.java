package com.hackathon.tutormatch.web.dto;

import com.hackathon.tutormatch.domain.model.Modalidad;

import java.time.Instant;
import java.util.List;

/**
 * Solicitud registrada con su asignacion. Si asignada = false, los campos del tutor y el score
 * vienen en null y la justificacion explica el motivo.
 */
public record SolicitudResponse(
        Long id,
        String nombreEstudiante,
        String materia,
        List<String> horarios,
        Modalidad modalidad,
        Instant fecha,
        boolean asignada,
        Long tutorAsignadoId,
        String nombreTutorAsignado,
        Double score,
        String justificacion,
        int candidatosEvaluados) {
}
