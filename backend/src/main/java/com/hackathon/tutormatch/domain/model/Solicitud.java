package com.hackathon.tutormatch.domain.model;

import com.hackathon.tutormatch.domain.exception.DatosInvalidosException;

import java.util.Set;

public record Solicitud(String nombreEstudiante, String materia, Set<String> horarios, Modalidad modalidad) {

    public Solicitud {
        if (Textos.estaVacio(nombreEstudiante)) {
            throw new DatosInvalidosException("El nombre del estudiante es obligatorio");
        }
        if (Textos.estaVacio(materia)) {
            throw new DatosInvalidosException("La materia es obligatoria");
        }
        if (modalidad == null) {
            throw new DatosInvalidosException("La modalidad es obligatoria");
        }
        nombreEstudiante = nombreEstudiante.trim();
        materia = materia.trim();
        horarios = Textos.horariosValidos(horarios);
    }
}
