package com.hackathon.tutormatch.domain.exception;

public class SolicitudNoEncontradaException extends RecursoNoEncontradoException {

    public SolicitudNoEncontradaException(Long id) {
        super("No existe una solicitud con id " + id);
    }
}
