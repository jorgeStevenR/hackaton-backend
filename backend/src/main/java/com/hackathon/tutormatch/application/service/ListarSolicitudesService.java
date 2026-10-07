package com.hackathon.tutormatch.application.service;

import com.hackathon.tutormatch.application.usecase.ListarSolicitudesUseCase;
import com.hackathon.tutormatch.domain.exception.SolicitudNoEncontradaException;
import com.hackathon.tutormatch.domain.model.SolicitudRegistrada;
import com.hackathon.tutormatch.domain.port.SolicitudRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListarSolicitudesService implements ListarSolicitudesUseCase {

    private final SolicitudRepositoryPort solicitudRepository;

    public ListarSolicitudesService(SolicitudRepositoryPort solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    @Override
    public List<SolicitudRegistrada> listar() {
        return solicitudRepository.buscarTodas();
    }

    @Override
    public SolicitudRegistrada obtenerPorId(Long id) {
        return solicitudRepository.buscarPorId(id)
                .orElseThrow(() -> new SolicitudNoEncontradaException(id));
    }
}
