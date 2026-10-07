package com.hackathon.tutormatch.web.dto;

public record DetalleCriterioResponse(
        String nombreCriterio,
        double peso,
        double puntaje,
        double aporte,
        String explicacion) {
}
