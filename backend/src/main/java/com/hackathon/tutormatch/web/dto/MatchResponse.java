package com.hackathon.tutormatch.web.dto;

import java.util.List;

/** disponible = el tutor comparte al menos un horario con el estudiante. */
public record MatchResponse(
        Long tutorId,
        String nombreTutor,
        double score,
        String justificacion,
        List<String> horariosCoincidentes,
        boolean recomendado,
        boolean disponible,
        List<DetalleCriterioResponse> desglose) {
}
