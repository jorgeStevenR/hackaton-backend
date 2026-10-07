package com.hackathon.tutormatch.application.service;

import com.hackathon.tutormatch.application.usecase.BuscarTutorIdealUseCase;
import com.hackathon.tutormatch.domain.matching.MotorAfinidad;
import com.hackathon.tutormatch.domain.model.ResultadoMatch;
import com.hackathon.tutormatch.domain.model.Solicitud;
import com.hackathon.tutormatch.domain.model.SolicitudRegistrada;
import com.hackathon.tutormatch.domain.model.Tutor;
import com.hackathon.tutormatch.domain.port.SolicitudRepositoryPort;
import com.hackathon.tutormatch.domain.port.TutorRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class BuscarTutorIdealService implements BuscarTutorIdealUseCase {

    private static final Logger log = LoggerFactory.getLogger(BuscarTutorIdealService.class);

    private final TutorRepositoryPort tutorRepository;
    private final SolicitudRepositoryPort solicitudRepository;
    private final MotorAfinidad motorAfinidad;

    public BuscarTutorIdealService(TutorRepositoryPort tutorRepository, SolicitudRepositoryPort solicitudRepository,
                                   MotorAfinidad motorAfinidad) {
        this.tutorRepository = tutorRepository;
        this.solicitudRepository = solicitudRepository;
        this.motorAfinidad = motorAfinidad;
    }

    /** Calcula el ranking y registra la solicitud con la asignacion resultante. */
    @Override
    @Transactional
    public List<ResultadoMatch> buscar(Solicitud solicitud) {
        List<Tutor> candidatos = tutorRepository.buscarPorMateria(solicitud.materia());
        List<ResultadoMatch> ranking = motorAfinidad.calcularRanking(solicitud, candidatos);

        SolicitudRegistrada registro = solicitudRepository.guardar(
                SolicitudRegistrada.desdeRanking(solicitud, ranking, Instant.now()));

        log.info("Solicitud id={} estudiante='{}' materia='{}' horarios={} modalidad={}: {} candidatos, asignado={} score={}",
                registro.id(), solicitud.nombreEstudiante(), solicitud.materia(), solicitud.horarios(),
                solicitud.modalidad(), ranking.size(),
                registro.asignada() ? "'" + registro.nombreTutorAsignado() + "'" : "ninguno", registro.score());
        return ranking;
    }
}
