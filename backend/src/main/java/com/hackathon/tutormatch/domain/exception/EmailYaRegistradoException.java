package com.hackathon.tutormatch.domain.exception;

public class EmailYaRegistradoException extends DomainException {

    public EmailYaRegistradoException() {
        super("Ya existe una cuenta con ese correo");
    }
}
