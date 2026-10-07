package com.hackathon.tutormatch.infrastructure.persistence;

import com.hackathon.tutormatch.domain.model.Solicitud;
import com.hackathon.tutormatch.domain.model.SolicitudRegistrada;
import com.hackathon.tutormatch.domain.port.SolicitudRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SolicitudPersistenceAdapter implements SolicitudRepositoryPort {

    private static final String SEPARADOR = ",";

    private final SpringDataSolicitudRepository repository;

    @Override
    public SolicitudRegistrada guardar(SolicitudRegistrada registro) {
        Solicitud s = registro.solicitud();
        SolicitudEntity entity = new SolicitudEntity(
                registro.id(), s.nombreEstudiante(), s.materia(), String.join(SEPARADOR, s.horarios()),
                s.modalidad(), registro.fecha(), registro.tutorAsignadoId(), registro.nombreTutorAsignado(),
                registro.score(), registro.justificacion(), registro.candidatosEvaluados());
        return aDominio(repository.save(entity));
    }

    @Override
    public List<SolicitudRegistrada> buscarTodas() {
        return repository.findAllByOrderByFechaDescIdDesc().stream()
                .map(SolicitudPersistenceAdapter::aDominio)
                .toList();
    }

    @Override
    public Optional<SolicitudRegistrada> buscarPorId(Long id) {
        return repository.findById(id).map(SolicitudPersistenceAdapter::aDominio);
    }

    private static SolicitudRegistrada aDominio(SolicitudEntity e) {
        Solicitud solicitud = new Solicitud(e.getNombreEstudiante(), e.getMateria(),
                new LinkedHashSet<>(Arrays.asList(e.getHorarios().split(SEPARADOR))), e.getModalidad());
        return new SolicitudRegistrada(e.getId(), solicitud, e.getFecha(), e.getTutorAsignadoId(),
                e.getNombreTutorAsignado(), e.getScore(), e.getJustificacion(), e.getCandidatosEvaluados());
    }
}
