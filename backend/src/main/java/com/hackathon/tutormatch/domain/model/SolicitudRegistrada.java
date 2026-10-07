package com.hackathon.tutormatch.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Historial de una solicitud: lo que pidio el estudiante y la asignacion que hizo el sistema.
 *
 * @param tutorAsignadoId    vacio si ningun tutor quedo recomendado
 * @param candidatosEvaluados tutores que dominaban la materia y fueron comparados
 */
public record SolicitudRegistrada(Long id,
                                  Solicitud solicitud,
                                  Instant fecha,
                                  Long tutorAsignadoId,
                                  String nombreTutorAsignado,
                                  Double score,
                                  String justificacion,
                                  int candidatosEvaluados) {

    private static final String SIN_TUTORES = "Ningún tutor domina la materia solicitada.";
    private static final String SIN_DISPONIBILIDAD =
            "Hay tutores que dominan la materia, pero ninguno coincide con los horarios solicitados.";

    /** Construye el registro a partir del ranking que calculo el motor. */
    public static SolicitudRegistrada desdeRanking(Solicitud solicitud, List<ResultadoMatch> ranking, Instant fecha) {
        Optional<ResultadoMatch> recomendado = ranking.stream().filter(ResultadoMatch::recomendado).findFirst();
        if (recomendado.isPresent()) {
            ResultadoMatch r = recomendado.get();
            return new SolicitudRegistrada(null, solicitud, fecha, r.tutor().getId(), r.tutor().getNombre(),
                    r.score(), r.justificacion(), ranking.size());
        }
        String motivo = ranking.isEmpty() ? SIN_TUTORES : SIN_DISPONIBILIDAD;
        return new SolicitudRegistrada(null, solicitud, fecha, null, null, null, motivo, ranking.size());
    }

    public boolean asignada() {
        return tutorAsignadoId != null;
    }
}
