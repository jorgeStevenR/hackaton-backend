package com.hackathon.tutormatch.domain.model;

import com.hackathon.tutormatch.domain.exception.DatosInvalidosException;

public enum NivelExperiencia {
    BASICO(1, "básico"),
    INTERMEDIO(2, "intermedio"),
    EXPERTO(3, "experto");

    public static final int VALOR_MAXIMO = 3;

    private final int valor;
    private final String etiqueta;

    NivelExperiencia(int valor, String etiqueta) {
        this.valor = valor;
        this.etiqueta = etiqueta;
    }

    public int valor() {
        return valor;
    }

    public String etiqueta() {
        return etiqueta;
    }

    public static NivelExperiencia desdeValor(int valor) {
        for (NivelExperiencia nivel : values()) {
            if (nivel.valor == valor) {
                return nivel;
            }
        }
        throw new DatosInvalidosException("El nivel debe estar entre 1 y 3, se recibio " + valor);
    }
}
