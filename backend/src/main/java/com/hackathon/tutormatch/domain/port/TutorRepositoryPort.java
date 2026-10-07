package com.hackathon.tutormatch.domain.port;

import com.hackathon.tutormatch.domain.model.Tutor;

import java.util.List;
import java.util.Optional;

/** Puerto de salida: el dominio define que necesita, la infraestructura decide como. */
public interface TutorRepositoryPort {

    Tutor guardar(Tutor tutor);

    List<Tutor> buscarTodos();

    Optional<Tutor> buscarPorId(Long id);

    /** Tutores que dominan la materia (sin distinguir mayusculas ni tildes). */
    List<Tutor> buscarPorMateria(String materia);
}
