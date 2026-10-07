package com.hackathon.tutormatch.domain.matching;

import com.hackathon.tutormatch.domain.exception.DatosInvalidosException;

/** Guarda y valida el peso comun a todos los criterios. */
abstract class CriterioBase implements CriterioAfinidad {

    private final double peso;

    protected CriterioBase(double peso) {
        if (peso < 0 || peso > 1) {
            throw new DatosInvalidosException("El peso del criterio debe estar entre 0 y 1, se recibio " + peso);
        }
        this.peso = peso;
    }

    @Override
    public double peso() {
        return peso;
    }
}
