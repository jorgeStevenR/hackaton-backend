package com.hackathon.tutormatch.domain.port;

import com.hackathon.tutormatch.domain.model.SolicitudRegistrada;

import java.util.List;
import java.util.Optional;

public interface SolicitudRepositoryPort {

    SolicitudRegistrada guardar(SolicitudRegistrada solicitud);

    /** Mas recientes primero. */
    List<SolicitudRegistrada> buscarTodas();

    Optional<SolicitudRegistrada> buscarPorId(Long id);
}
