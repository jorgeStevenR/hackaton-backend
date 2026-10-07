package com.hackathon.tutormatch.application.service;

import com.hackathon.tutormatch.application.usecase.ListarTutoresUseCase;
import com.hackathon.tutormatch.domain.exception.TutorNoEncontradoException;
import com.hackathon.tutormatch.domain.model.Tutor;
import com.hackathon.tutormatch.domain.port.TutorRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListarTutoresService implements ListarTutoresUseCase {

    private final TutorRepositoryPort tutorRepository;

    public ListarTutoresService(TutorRepositoryPort tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    @Override
    public List<Tutor> listar() {
        return tutorRepository.buscarTodos();
    }

    @Override
    public Tutor obtenerPorId(Long id) {
        return tutorRepository.buscarPorId(id)
                .orElseThrow(() -> new TutorNoEncontradoException(id));
    }
}
