package com.hackathon.tutormatch.domain.matching;

import com.hackathon.tutormatch.domain.model.Solicitud;
import com.hackathon.tutormatch.domain.model.Tutor;

import java.util.Set;

public class CriterioHorario extends CriterioBase {

    public CriterioHorario(double peso) {
        super(peso);
    }

    @Override
    public String nombre() {
        return "Horario";
    }

    @Override
    public double evaluar(Tutor tutor, Solicitud solicitud) {
        int solicitados = solicitud.horarios().size();
        return (double) tutor.horariosEnComun(solicitud.horarios()).size() / solicitados;
    }

    @Override
    public String explicar(Tutor tutor, Solicitud solicitud) {
        Set<String> comunes = tutor.horariosEnComun(solicitud.horarios());
        if (comunes.isEmpty()) {
            return "no coincide en ningún horario";
        }
        return "coincide en " + comunes.size() + " de " + solicitud.horarios().size()
                + " horarios (" + String.join(", ", comunes) + ")";
    }
}
