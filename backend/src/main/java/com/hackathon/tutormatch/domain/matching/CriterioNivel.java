package com.hackathon.tutormatch.domain.matching;

import com.hackathon.tutormatch.domain.model.NivelExperiencia;
import com.hackathon.tutormatch.domain.model.Solicitud;
import com.hackathon.tutormatch.domain.model.Tutor;

public class CriterioNivel extends CriterioBase {

    public CriterioNivel(double peso) {
        super(peso);
    }

    @Override
    public String nombre() {
        return "Nivel";
    }

    @Override
    public double evaluar(Tutor tutor, Solicitud solicitud) {
        return (double) tutor.getNivel().valor() / NivelExperiencia.VALOR_MAXIMO;
    }

    @Override
    public String explicar(Tutor tutor, Solicitud solicitud) {
        return "nivel " + tutor.getNivel().etiqueta();
    }
}
