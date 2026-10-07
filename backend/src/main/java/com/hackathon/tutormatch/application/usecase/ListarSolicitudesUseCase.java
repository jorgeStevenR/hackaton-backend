package com.hackathon.tutormatch.application.usecase;

import com.hackathon.tutormatch.domain.model.SolicitudRegistrada;

import java.util.List;

public interface ListarSolicitudesUseCase {

    /** Historial de solicitudes, mas recientes primero. */
    List<SolicitudRegistrada> listar();

    /** @throws com.hackathon.tutormatch.domain.exception.SolicitudNoEncontradaException si no existe */
    SolicitudRegistrada obtenerPorId(Long id);
}
