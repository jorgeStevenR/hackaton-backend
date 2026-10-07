package com.hackathon.tutormatch.domain.matching;

import com.hackathon.tutormatch.domain.exception.DatosInvalidosException;
import com.hackathon.tutormatch.domain.model.Modalidad;
import com.hackathon.tutormatch.domain.model.NivelExperiencia;
import com.hackathon.tutormatch.domain.model.ResultadoMatch;
import com.hackathon.tutormatch.domain.model.Solicitud;
import com.hackathon.tutormatch.domain.model.Tutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MotorAfinidadTest {

    private MotorAfinidad motor;

    @BeforeEach
    void setUp() {
        motor = new MotorAfinidad(List.of(
                new CriterioHorario(0.40),
                new CriterioNivel(0.30),
                new CriterioCalificacion(0.20),
                new CriterioModalidad(0.10)));
    }

    @Test
    @DisplayName("Match perfecto obtiene score 100")
    void matchPerfecto() {
        Tutor perfecto = tutor(1L, "Ana", "Calculo", List.of("LUN-08", "MIE-10"),
                NivelExperiencia.EXPERTO, 5.0, Modalidad.AMBAS);

        List<ResultadoMatch> ranking = motor.calcularRanking(
                solicitud("Calculo", "LUN-08", "MIE-10"), List.of(perfecto));

        assertThat(ranking).hasSize(1);
        ResultadoMatch resultado = ranking.get(0);
        assertThat(resultado.score()).isEqualTo(100.0);
        assertThat(resultado.horariosCoincidentes()).containsExactly("LUN-08", "MIE-10");
        assertThat(resultado.desglose()).hasSize(4);
        assertThat(resultado.justificacion())
                .startsWith("Domina Calculo, coincide en 2 de 2 horarios (LUN-08, MIE-10)")
                .endsWith(".");
    }

    @Test
    @DisplayName("Tutor que no domina la materia queda excluido")
    void excluyeTutorSinMateria() {
        Tutor deCalculo = tutor(1L, "Ana", "Calculo", List.of("LUN-08"), NivelExperiencia.BASICO, 3.0, Modalidad.VIRTUAL);
        Tutor deIngles = tutor(2L, "Sofia", "Ingles", List.of("LUN-08"), NivelExperiencia.EXPERTO, 5.0, Modalidad.AMBAS);

        List<ResultadoMatch> ranking = motor.calcularRanking(solicitud("Calculo", "LUN-08"), List.of(deCalculo, deIngles));

        assertThat(ranking).extracting(r -> r.tutor().getNombre()).containsExactly("Ana");
    }

    @Test
    @DisplayName("Ningun tutor para la materia devuelve lista vacia")
    void sinTutoresParaLaMateria() {
        Tutor deIngles = tutor(1L, "Sofia", "Ingles", List.of("LUN-08"), NivelExperiencia.EXPERTO, 5.0, Modalidad.AMBAS);

        List<ResultadoMatch> ranking = motor.calcularRanking(solicitud("Quimica", "LUN-08"), List.of(deIngles));

        assertThat(ranking).isEmpty();
    }

    @Test
    @DisplayName("Empate de score: gana el de mayor nivel")
    void desempatePorNivel() {
        // Intermedio: 40 + 20 + 20*(5/5) + 10 = 90
        Tutor intermedio = tutor(1L, "Carlos", "Calculo", List.of("LUN-08"), NivelExperiencia.INTERMEDIO, 5.0, Modalidad.AMBAS);
        // Experto:    40 + 30 + 20*(2.5/5) + 10 = 90
        Tutor experto = tutor(2L, "Ana", "Calculo", List.of("LUN-08"), NivelExperiencia.EXPERTO, 2.5, Modalidad.AMBAS);

        List<ResultadoMatch> ranking = motor.calcularRanking(solicitud("Calculo", "LUN-08"), List.of(intermedio, experto));

        assertThat(ranking).extracting(ResultadoMatch::score).containsExactly(90.0, 90.0);
        assertThat(ranking.get(0).tutor().getNombre()).isEqualTo("Ana");
    }

    @Test
    @DisplayName("El primero del ranking queda recomendado y el resto no")
    void primeroRecomendado() {
        Tutor bueno = tutor(1L, "Ana", "Calculo", List.of("LUN-08", "MIE-10"), NivelExperiencia.EXPERTO, 4.8, Modalidad.AMBAS);
        Tutor regular = tutor(2L, "Mateo", "Calculo", List.of("JUE-16"), NivelExperiencia.BASICO, 3.0, Modalidad.PRESENCIAL);

        List<ResultadoMatch> ranking = motor.calcularRanking(solicitud("Calculo", "LUN-08", "MIE-10"), List.of(regular, bueno));

        assertThat(ranking.get(0).tutor().getNombre()).isEqualTo("Ana");
        assertThat(ranking.get(0).recomendado()).isTrue();
        assertThat(ranking.get(1).recomendado()).isFalse();
        assertThat(ranking.get(0).score()).isGreaterThan(ranking.get(1).score());
    }

    @Test
    @DisplayName("La materia se compara sin mayusculas ni tildes")
    void materiaSinTildes() {
        Tutor tutor = tutor(1L, "Ana", "Calculo", List.of("LUN-08"), NivelExperiencia.EXPERTO, 5.0, Modalidad.AMBAS);

        assertThat(tutor.dominaMateria("CÁLCULO")).isTrue();
        assertThat(motor.calcularRanking(solicitud("cálculo", "LUN-08"), List.of(tutor))).hasSize(1);
    }

    @Test
    @DisplayName("Horario con formato invalido es rechazado por el dominio")
    void horarioInvalido() {
        assertThatThrownBy(() -> solicitud("Calculo", "SAB-08"))
                .isInstanceOf(DatosInvalidosException.class);
    }

    private static Tutor tutor(Long id, String nombre, String materia, List<String> horarios,
                               NivelExperiencia nivel, double calificacion, Modalidad modalidad) {
        return new Tutor(id, nombre, List.of(materia), horarios, nivel, calificacion, modalidad);
    }

    private static Solicitud solicitud(String materia, String... horarios) {
        return new Solicitud("Pedro", materia, new LinkedHashSet<>(List.of(horarios)), Modalidad.VIRTUAL);
    }
}
