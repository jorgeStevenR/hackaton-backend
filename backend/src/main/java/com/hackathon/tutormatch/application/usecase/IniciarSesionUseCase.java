package com.hackathon.tutormatch.application.usecase;

import com.hackathon.tutormatch.domain.model.Sesion;

public interface IniciarSesionUseCase {

    /** @throws com.hackathon.tutormatch.domain.exception.CredencialesInvalidasException si el correo o la contrasena no coinciden */
    Sesion iniciarSesion(String email, String password, boolean recordarSesion);
}
