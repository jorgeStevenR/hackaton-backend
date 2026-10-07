package com.hackathon.tutormatch.domain.matching;

import com.hackathon.tutormatch.domain.model.Solicitud;
import com.hackathon.tutormatch.domain.model.Tutor;

public class CriterioModalidad extends CriterioBase {

    public CriterioModalidad(double peso) {
        super(peso);
    }

    @Override
    public String nombre() {
        return "Modalidad";
    }

    @Override
    public double evaluar(Tutor tutor, Solicitud solicitud) {
        return tutor.getModalidad().esCompatibleCon(solicitud.modalidad()) ? 1.0 : 0.0;
    }

    @Override
    public String explicar(Tutor tutor, Solicitud solicitud) {
        return tutor.getModalidad().esCompatibleCon(solicitud.modalidad())
                ? "modalidad compatible (" + tutor.getModalidad() + ")"
                : "modalidad distinta (" + tutor.getModalidad() + ")";
    }
}
