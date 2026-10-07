package com.hackathon.tutormatch.domain.matching;

import com.hackathon.tutormatch.domain.model.Solicitud;
import com.hackathon.tutormatch.domain.model.Tutor;

/** Estrategia de evaluacion: cada criterio aporta un puntaje ponderado al score final. */
public interface CriterioAfinidad {

    String nombre();

    /** Peso del criterio, entre 0 y 1. La suma de todos los pesos debe ser 1. */
    double peso();

    /** Puntaje entre 0.0 y 1.0. */
    double evaluar(Tutor tutor, Solicitud solicitud);

    /** Fragmento de justificacion en espanol, ej. "nivel experto". */
    String explicar(Tutor tutor, Solicitud solicitud);
}
