package com.hackathon.tutormatch.application.service;

import com.hackathon.tutormatch.application.usecase.BuscarTutorIdealUseCase;
import com.hackathon.tutormatch.domain.matching.MotorAfinidad;
import com.hackathon.tutormatch.domain.model.ResultadoMatch;
import com.hackathon.tutormatch.domain.model.Solicitud;
import com.hackathon.tutormatch.domain.model.Tutor;
import com.hackathon.tutormatch.domain.port.TutorRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BuscarTutorIdealService implements BuscarTutorIdealUseCase {

    private static final Logger log = LoggerFactory.getLogger(BuscarTutorIdealService.class);

    private final TutorRepositoryPort tutorRepository;
    private final MotorAfinidad motorAfinidad;

    public BuscarTutorIdealService(TutorRepositoryPort tutorRepository, MotorAfinidad motorAfinidad) {
        this.tutorRepository = tutorRepository;
        this.motorAfinidad = motorAfinidad;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoMatch> buscar(Solicitud solicitud) {
        List<Tutor> candidatos = tutorRepository.buscarPorMateria(solicitud.materia());
        List<ResultadoMatch> ranking = motorAfinidad.calcularRanking(solicitud, candidatos);

        if (ranking.isEmpty()) {
            log.info("Match estudiante='{}' materia='{}' horarios={} modalidad={}: ningun tutor domina la materia",
                    solicitud.nombreEstudiante(), solicitud.materia(), solicitud.horarios(), solicitud.modalidad());
        } else {
            ResultadoMatch mejor = ranking.get(0);
            log.info("Match estudiante='{}' materia='{}' horarios={} modalidad={}: {} candidatos, recomendado='{}' score={}",
                    solicitud.nombreEstudiante(), solicitud.materia(), solicitud.horarios(), solicitud.modalidad(),
                    ranking.size(), mejor.tutor().getNombre(), mejor.score());
        }
        return ranking;
    }
}
