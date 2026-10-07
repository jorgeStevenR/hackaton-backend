package com.hackathon.tutormatch.application.usecase;

import com.hackathon.tutormatch.domain.model.Usuario;

public interface ObtenerUsuarioActualUseCase {

    /** @throws com.hackathon.tutormatch.domain.exception.SesionExpiradaException si el token no es valido o vencio */
    Usuario obtenerPorToken(String token);
}
