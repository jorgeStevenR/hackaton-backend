package com.hackathon.tutormatch.domain.matching;

import com.hackathon.tutormatch.domain.model.Solicitud;
import com.hackathon.tutormatch.domain.model.Tutor;

import java.util.Locale;

public class CriterioCalificacion extends CriterioBase {

    private static final double CALIFICACION_MAXIMA = 5.0;

    public CriterioCalificacion(double peso) {
        super(peso);
    }

    @Override
    public String nombre() {
        return "Calificación";
    }

    @Override
    public double evaluar(Tutor tutor, Solicitud solicitud) {
        return tutor.getCalificacion() / CALIFICACION_MAXIMA;
    }

    @Override
    public String explicar(Tutor tutor, Solicitud solicitud) {
        return String.format(Locale.US, "calificación %.1f/5", tutor.getCalificacion());
    }
}
