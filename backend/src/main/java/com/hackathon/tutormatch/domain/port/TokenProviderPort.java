package com.hackathon.tutormatch.domain.port;

import com.hackathon.tutormatch.domain.model.Usuario;

import java.util.Optional;

public interface TokenProviderPort {

    /** @param recordarSesion true para un token de mayor duracion ("recordarme") */
    String generar(Usuario usuario, boolean recordarSesion);

    /** Id del usuario si el token es valido y no ha vencido; vacio en otro caso. */
    Optional<Long> validar(String token);
}
