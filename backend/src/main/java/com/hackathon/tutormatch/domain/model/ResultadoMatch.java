package com.hackathon.tutormatch.domain.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public record ResultadoMatch(Tutor tutor,
                             double score,
                             List<DetalleCriterio> desglose,
                             String justificacion,
                             Set<String> horariosCoincidentes,
                             boolean recomendado) {

    public ResultadoMatch {
        desglose = List.copyOf(desglose);
        // Copia inmutable que conserva el orden
        horariosCoincidentes = Collections.unmodifiableSet(new LinkedHashSet<>(horariosCoincidentes));
    }

    public ResultadoMatch comoRecomendado() {
        return new ResultadoMatch(tutor, score, desglose, justificacion, horariosCoincidentes, true);
    }
}
