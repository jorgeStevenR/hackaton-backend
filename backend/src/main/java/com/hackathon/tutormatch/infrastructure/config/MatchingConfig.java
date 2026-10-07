package com.hackathon.tutormatch.infrastructure.config;

import com.hackathon.tutormatch.domain.matching.CriterioAfinidad;
import com.hackathon.tutormatch.domain.matching.CriterioCalificacion;
import com.hackathon.tutormatch.domain.matching.CriterioHorario;
import com.hackathon.tutormatch.domain.matching.CriterioModalidad;
import com.hackathon.tutormatch.domain.matching.CriterioNivel;
import com.hackathon.tutormatch.domain.matching.MotorAfinidad;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/** Ensambla el motor de afinidad: asi el dominio queda libre de Spring. */
@Configuration
@EnableConfigurationProperties(MatchingProperties.class)
public class MatchingConfig {

    @Bean
    public CriterioHorario criterioHorario(MatchingProperties pesos) {
        return new CriterioHorario(pesos.horario());
    }

    @Bean
    public CriterioNivel criterioNivel(MatchingProperties pesos) {
        return new CriterioNivel(pesos.nivel());
    }

    @Bean
    public CriterioCalificacion criterioCalificacion(MatchingProperties pesos) {
        return new CriterioCalificacion(pesos.calificacion());
    }

    @Bean
    public CriterioModalidad criterioModalidad(MatchingProperties pesos) {
        return new CriterioModalidad(pesos.modalidad());
    }

    /** El orden de la lista es el orden en que aparecen en el desglose y la justificacion. */
    @Bean
    public MotorAfinidad motorAfinidad(CriterioHorario horario, CriterioNivel nivel,
                                       CriterioCalificacion calificacion, CriterioModalidad modalidad) {
        List<CriterioAfinidad> criterios = List.of(horario, nivel, calificacion, modalidad);
        return new MotorAfinidad(criterios);
    }
}
