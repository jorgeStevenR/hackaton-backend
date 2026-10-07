package com.hackathon.tutormatch.application.service;

import com.hackathon.tutormatch.application.usecase.RegistrarTutorUseCase;
import com.hackathon.tutormatch.domain.model.Tutor;
import com.hackathon.tutormatch.domain.port.TutorRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrarTutorService implements RegistrarTutorUseCase {

    private static final Logger log = LoggerFactory.getLogger(RegistrarTutorService.class);

    private final TutorRepositoryPort tutorRepository;

    public RegistrarTutorService(TutorRepositoryPort tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    @Override
    @Transactional
    public Tutor registrar(Tutor tutor) {
        Tutor guardado = tutorRepository.guardar(tutor);
        log.info("Tutor registrado id={} nombre='{}' materias={}", guardado.getId(), guardado.getNombre(), guardado.getMaterias());
        return guardado;
    }
}
