package com.hackathon.tutormatch.domain.exception;

public class SesionExpiradaException extends DomainException {

    public SesionExpiradaException() {
        super("Sesión expirada");
    }
}
