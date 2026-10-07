package com.hackathon.tutormatch.web.dto;

import java.util.List;

public record MatchResponse(
        Long tutorId,
        String nombreTutor,
        double score,
        String justificacion,
        List<String> horariosCoincidentes,
        boolean recomendado,
        List<DetalleCriterioResponse> desglose) {
}
