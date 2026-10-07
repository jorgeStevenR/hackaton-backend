package com.hackathon.tutormatch.domain.exception;

/** Base de los errores "no existe" (se responden con 404). */
public class RecursoNoEncontradoException extends DomainException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
