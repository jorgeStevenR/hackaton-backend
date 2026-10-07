package com.hackathon.tutormatch.infrastructure.persistence;

import com.hackathon.tutormatch.domain.model.Modalidad;
import com.hackathon.tutormatch.domain.model.NivelExperiencia;
import com.hackathon.tutormatch.domain.model.Tutor;
import com.hackathon.tutormatch.domain.port.TutorRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.hackathon.tutormatch.domain.model.Modalidad.AMBAS;
import static com.hackathon.tutormatch.domain.model.Modalidad.PRESENCIAL;
import static com.hackathon.tutormatch.domain.model.Modalidad.VIRTUAL;
import static com.hackathon.tutormatch.domain.model.NivelExperiencia.BASICO;
import static com.hackathon.tutormatch.domain.model.NivelExperiencia.EXPERTO;
import static com.hackathon.tutormatch.domain.model.NivelExperiencia.INTERMEDIO;

/** Carga 10 tutores de ejemplo con JPA al arrancar, solo si la tabla esta vacia. */
@Slf4j
@Component
@RequiredArgsConstructor
public class DatosIniciales implements ApplicationRunner {

    private final TutorRepositoryPort tutorRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (!tutorRepository.buscarTodos().isEmpty()) {
            return;
        }
        List<Tutor> tutores = List.of(
                tutor("Ana Martinez", "Calculo,Fisica", "LUN-08,MIE-10,VIE-14", EXPERTO, 4.8, AMBAS),
                tutor("Carlos Gomez", "Calculo,Estadistica", "LUN-08,MAR-16,JUE-10", INTERMEDIO, 4.5, PRESENCIAL),
                tutor("Laura Rodriguez", "Programacion,Bases de Datos", "MAR-08,JUE-14,VIE-10", EXPERTO, 4.9, VIRTUAL),
                tutor("Diego Herrera", "Programacion", "LUN-10,MIE-10,VIE-16", BASICO, 4.2, AMBAS),
                tutor("Sofia Ramirez", "Ingles", "LUN-08,MAR-10,MIE-08,JUE-10", EXPERTO, 4.7, VIRTUAL),
                tutor("Mateo Lopez", "Fisica,Calculo", "MIE-10,JUE-16,VIE-08", INTERMEDIO, 3.9, PRESENCIAL),
                tutor("Valentina Cruz", "Estadistica,Bases de Datos", "MAR-14,MIE-16,VIE-14", INTERMEDIO, 4.6, AMBAS),
                tutor("Andres Torres", "Calculo", "LUN-08,MIE-10", BASICO, 5.0, VIRTUAL),
                tutor("Camila Vargas", "Ingles,Estadistica", "LUN-14,MIE-14,VIE-10", BASICO, 4.4, PRESENCIAL),
                tutor("Julian Castro", "Programacion,Fisica,Calculo", "MAR-08,MIE-10,JUE-14", EXPERTO, 4.1, AMBAS));
        tutores.forEach(tutorRepository::guardar);
        log.info("Datos iniciales: {} tutores cargados", tutores.size());
    }

    private static Tutor tutor(String nombre, String materias, String horarios,
                               NivelExperiencia nivel, double calificacion, Modalidad modalidad) {
        return new Tutor(null, nombre, List.of(materias.split(",")), List.of(horarios.split(",")),
                nivel, calificacion, modalidad);
    }
}
