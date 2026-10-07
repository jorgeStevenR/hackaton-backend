package com.hackathon.tutormatch.domain.exception;

public class CredencialesInvalidasException extends DomainException {

    public CredencialesInvalidasException() {
        super("Correo o contraseña incorrectos");
    }
}
