package com.hackathon.tutormatch.domain.matching;

import com.hackathon.tutormatch.domain.exception.DatosInvalidosException;
import com.hackathon.tutormatch.domain.model.DetalleCriterio;
import com.hackathon.tutormatch.domain.model.ResultadoMatch;
import com.hackathon.tutormatch.domain.model.Solicitud;
import com.hackathon.tutormatch.domain.model.Tutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Calcula la afinidad entre una solicitud y los tutores aplicando una lista de criterios (Strategy).
 * score = Σ(evaluar x peso) x 100, redondeado a 1 decimal.
 */
public class MotorAfinidad {

    /** Primero los tutores disponibles; luego mayor score; en empate, mayor nivel. */
    private static final Comparator<ResultadoMatch> RANKING = Comparator
            .comparing(ResultadoMatch::disponible).reversed()
            .thenComparing(Comparator.comparingDouble(ResultadoMatch::score).reversed())
            .thenComparing(Comparator.comparingInt((ResultadoMatch r) -> r.tutor().getNivel().valor()).reversed());

    private final List<CriterioAfinidad> criterios;

    public MotorAfinidad(List<CriterioAfinidad> criterios) {
        if (criterios == null || criterios.isEmpty()) {
            throw new DatosInvalidosException("El motor necesita al menos un criterio de afinidad");
        }
        this.criterios = List.copyOf(criterios);
    }

    /**
     * Ranking de mayor a menor afinidad. Se recomienda al primero solo si esta disponible
     * (comparte al menos un horario): si nadie lo esta, ninguno queda recomendado.
     * Lista vacia si nadie domina la materia.
     */
    public List<ResultadoMatch> calcularRanking(Solicitud solicitud, Collection<Tutor> tutores) {
        List<ResultadoMatch> ranking = new ArrayList<>();
        for (Tutor tutor : tutores) {
            tutor.materiaEquivalente(solicitud.materia())
                    .ifPresent(materia -> ranking.add(evaluar(tutor, materia, solicitud)));
        }
        ranking.sort(RANKING);
        if (!ranking.isEmpty() && ranking.get(0).disponible()) {
            ranking.set(0, ranking.get(0).comoRecomendado());
        }
        return List.copyOf(ranking);
    }

    public List<CriterioAfinidad> getCriterios() {
        return criterios;
    }

    private ResultadoMatch evaluar(Tutor tutor, String materia, Solicitud solicitud) {
        List<DetalleCriterio> desglose = new ArrayList<>();
        List<String> explicaciones = new ArrayList<>();
        double total = 0;
        for (CriterioAfinidad criterio : criterios) {
            double puntaje = Math.max(0, Math.min(1, criterio.evaluar(tutor, solicitud)));
            double aporte = puntaje * criterio.peso() * 100;
            String explicacion = criterio.explicar(tutor, solicitud);
            total += aporte;
            desglose.add(new DetalleCriterio(criterio.nombre(), criterio.peso(),
                    redondear(puntaje, 3), redondear(aporte, 1), explicacion));
            explicaciones.add(explicacion);
        }
        double score = Math.min(100, redondear(total, 1));
        String justificacion = "Domina " + materia + ", " + unir(explicaciones) + ".";
        return new ResultadoMatch(tutor, score, desglose, justificacion,
                tutor.horariosEnComun(solicitud.horarios()), false);
    }

    /** ["a", "b", "c"] -> "a, b y c" */
    private static String unir(List<String> partes) {
        if (partes.size() == 1) {
            return partes.get(0);
        }
        return String.join(", ", partes.subList(0, partes.size() - 1)) + " y " + partes.get(partes.size() - 1);
    }

    private static double redondear(double valor, int decimales) {
        double factor = Math.pow(10, decimales);
        return Math.round(valor * factor) / factor;
    }
}
