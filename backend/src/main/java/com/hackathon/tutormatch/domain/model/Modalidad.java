package com.hackathon.tutormatch.domain.model;

public enum Modalidad {
    PRESENCIAL,
    VIRTUAL,
    AMBAS;

    /** AMBAS es compatible con cualquier modalidad. */
    public boolean esCompatibleCon(Modalidad otra) {
        if (otra == null) {
            return false;
        }
        return this == AMBAS || otra == AMBAS || this == otra;
    }
}
