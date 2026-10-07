package com.hackathon.tutormatch.application.usecase;

import com.hackathon.tutormatch.domain.model.Tutor;

import java.util.List;

public interface ListarTutoresUseCase {

    List<Tutor> listar();

    /** @throws com.hackathon.tutormatch.domain.exception.TutorNoEncontradoException si no existe */
    Tutor obtenerPorId(Long id);
}
