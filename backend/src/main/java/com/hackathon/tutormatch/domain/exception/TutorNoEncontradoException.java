package com.hackathon.tutormatch.domain.exception;

public class TutorNoEncontradoException extends RecursoNoEncontradoException {

    public TutorNoEncontradoException(Long id) {
        super("No existe un tutor con id " + id);
    }
}
