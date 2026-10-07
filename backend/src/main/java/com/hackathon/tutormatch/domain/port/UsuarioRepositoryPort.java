package com.hackathon.tutormatch.domain.port;

import com.hackathon.tutormatch.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioRepositoryPort {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorId(Long id);

    /** El email debe venir ya normalizado (minusculas). */
    Optional<Usuario> buscarPorEmail(String email);

    boolean existePorEmail(String email);
}
