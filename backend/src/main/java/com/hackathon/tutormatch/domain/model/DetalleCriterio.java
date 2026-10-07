package com.hackathon.tutormatch.domain.model;

/**
 * Resultado de un criterio para un tutor.
 *
 * @param puntaje entre 0 y 1
 * @param aporte  puntos sobre 100 que suma al score (puntaje x peso x 100)
 */
public record DetalleCriterio(String nombreCriterio, double peso, double puntaje, double aporte, String explicacion) {
}
