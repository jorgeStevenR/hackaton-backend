package com.hackathon.tutormatch.domain.exception;

public class TutorNoEncontradoException extends DomainException {

    public TutorNoEncontradoException(Long id) {
        super("No existe un tutor con id " + id);
    }
}
