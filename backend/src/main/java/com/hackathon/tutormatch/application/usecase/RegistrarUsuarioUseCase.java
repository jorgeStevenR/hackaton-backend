package com.hackathon.tutormatch.application.usecase;

import com.hackathon.tutormatch.domain.model.Sesion;

public interface RegistrarUsuarioUseCase {

    /** Crea la cuenta y deja la sesion iniciada. */
    Sesion registrar(String nombre, String email, String password);
}
