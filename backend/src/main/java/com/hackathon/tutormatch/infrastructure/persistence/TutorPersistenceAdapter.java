package com.hackathon.tutormatch.infrastructure.persistence;

import com.hackathon.tutormatch.domain.model.Tutor;
import com.hackathon.tutormatch.domain.port.TutorRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/** Adaptador de salida: implementa el puerto del dominio con Spring Data JPA. */
@Component
@RequiredArgsConstructor
public class TutorPersistenceAdapter implements TutorRepositoryPort {

    private final SpringDataTutorRepository repository;
    private final TutorEntityMapper mapper;

    @Override
    public Tutor guardar(Tutor tutor) {
        return mapper.aDominio(repository.save(mapper.aEntidad(tutor)));
    }

    @Override
    public List<Tutor> buscarTodos() {
        return repository.findAll(Sort.by("id")).stream().map(mapper::aDominio).toList();
    }

    @Override
    public Optional<Tutor> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::aDominio);
    }

    @Override
    public List<Tutor> buscarPorMateria(String materia) {
        // La comparacion sin tildes es una regla del dominio: se delega en Tutor.dominaMateria
        return buscarTodos().stream().filter(t -> t.dominaMateria(materia)).toList();
    }
}
