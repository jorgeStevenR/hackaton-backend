package com.hackathon.tutormatch.web.dto;

import com.hackathon.tutormatch.domain.model.Modalidad;

public record TutorResponse(
        Long id,
        String nombre,
        String materias,
        String horarios,
        int nivel,
        double calificacion,
        Modalidad modalidad) {
}
