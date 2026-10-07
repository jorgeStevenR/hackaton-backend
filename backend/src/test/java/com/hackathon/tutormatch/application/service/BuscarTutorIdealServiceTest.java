package com.hackathon.tutormatch.application.service;

import com.hackathon.tutormatch.domain.matching.CriterioCalificacion;
import com.hackathon.tutormatch.domain.matching.CriterioHorario;
import com.hackathon.tutormatch.domain.matching.CriterioModalidad;
import com.hackathon.tutormatch.domain.matching.CriterioNivel;
import com.hackathon.tutormatch.domain.matching.MotorAfinidad;
import com.hackathon.tutormatch.domain.model.Modalidad;
import com.hackathon.tutormatch.domain.model.NivelExperiencia;
import com.hackathon.tutormatch.domain.model.Solicitud;
import com.hackathon.tutormatch.domain.model.SolicitudRegistrada;
import com.hackathon.tutormatch.domain.model.Tutor;
import com.hackathon.tutormatch.domain.port.SolicitudRepositoryPort;
import com.hackathon.tutormatch.domain.port.TutorRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/** Prueba el caso de uso con repositorios en memoria, sin Spring ni base de datos. */
class BuscarTutorIdealServiceTest {

    private final List<Tutor> tutores = new ArrayList<>();
    private final List<SolicitudRegistrada> registradas = new ArrayList<>();
    private BuscarTutorIdealService service;

    @BeforeEach
    void setUp() {
        TutorRepositoryPort tutorRepo = new TutorRepositoryPort() {
            public Tutor guardar(Tutor tutor) { tutores.add(tutor); return tutor; }
            public List<Tutor> buscarTodos() { return tutores; }
            public Optional<Tutor> buscarPorId(Long id) { return tutores.stream().filter(t -> t.getId().equals(id)).findFirst(); }
            public List<Tutor> buscarPorMateria(String materia) { return tutores.stream().filter(t -> t.dominaMateria(materia)).toList(); }
        };
        SolicitudRepositoryPort solicitudRepo = new SolicitudRepositoryPort() {
            public SolicitudRegistrada guardar(SolicitudRegistrada s) {
                SolicitudRegistrada conId = new SolicitudRegistrada((long) registradas.size() + 1, s.solicitud(), s.fecha(),
                        s.tutorAsignadoId(), s.nombreTutorAsignado(), s.score(), s.justificacion(), s.candidatosEvaluados());
                registradas.add(conId);
                return conId;
            }
            public List<SolicitudRegistrada> buscarTodas() { return registradas; }
            public Optional<SolicitudRegistrada> buscarPorId(Long id) { return Optional.empty(); }
        };
        MotorAfinidad motor = new MotorAfinidad(List.of(new CriterioHorario(0.4), new CriterioNivel(0.3),
                new CriterioCalificacion(0.2), new CriterioModalidad(0.1)));
        service = new BuscarTutorIdealService(tutorRepo, solicitudRepo, motor);

        tutores.add(new Tutor(1L, "Ana", List.of("Calculo"), List.of("LUN-08"), NivelExperiencia.EXPERTO, 4.8, Modalidad.AMBAS));
        tutores.add(new Tutor(2L, "Sofia", List.of("Ingles"), List.of("LUN-08"), NivelExperiencia.EXPERTO, 4.7, Modalidad.VIRTUAL));
    }

    @Test
    @DisplayName("Cada busqueda queda registrada con el tutor asignado")
    void registraSolicitudConAsignacion() {
        service.buscar(solicitud("Calculo", "LUN-08"));

        assertThat(registradas).hasSize(1);
        SolicitudRegistrada registro = registradas.get(0);
        assertThat(registro.asignada()).isTrue();
        assertThat(registro.nombreTutorAsignado()).isEqualTo("Ana");
        assertThat(registro.score()).isNotNull();
        assertThat(registro.justificacion()).startsWith("Domina Calculo");
        assertThat(registro.candidatosEvaluados()).isEqualTo(1);
    }

    @Test
    @DisplayName("Una solicitud sin tutores tambien se registra, sin asignacion y con el motivo")
    void registraSolicitudSinTutores() {
        service.buscar(solicitud("Quimica", "LUN-08"));

        SolicitudRegistrada registro = registradas.get(0);
        assertThat(registro.asignada()).isFalse();
        assertThat(registro.score()).isNull();
        assertThat(registro.justificacion()).contains("Ningún tutor domina");
    }

    @Test
    @DisplayName("Si nadie tiene horario en comun se registra sin asignacion")
    void registraSolicitudSinDisponibilidad() {
        service.buscar(solicitud("Calculo", "VIE-16"));

        SolicitudRegistrada registro = registradas.get(0);
        assertThat(registro.asignada()).isFalse();
        assertThat(registro.candidatosEvaluados()).isEqualTo(1);
        assertThat(registro.justificacion()).contains("ninguno coincide con los horarios");
    }

    private static Solicitud solicitud(String materia, String... horarios) {
        return new Solicitud("Pedro", materia, new LinkedHashSet<>(List.of(horarios)), Modalidad.VIRTUAL);
    }
}
