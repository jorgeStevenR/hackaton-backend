package com.hackathon.tutormatch.domain.exception;

/** Violacion de una regla de negocio. */
public class DomainException extends RuntimeException {

    public DomainException(String mensaje) {
        super(mensaje);
    }
}
