package com.hackathon.tutormatch.infrastructure.persistence;

import com.hackathon.tutormatch.domain.model.NivelExperiencia;
import com.hackathon.tutormatch.domain.model.Tutor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class TutorEntityMapper {

    private static final String SEPARADOR = ",";

    public Tutor aDominio(TutorEntity entity) {
        return new Tutor(
                entity.getId(),
                entity.getNombre(),
                separar(entity.getMaterias()),
                separar(entity.getHorarios()),
                NivelExperiencia.desdeValor(entity.getNivel()),
                entity.getCalificacion(),
                entity.getModalidad());
    }

    public TutorEntity aEntidad(Tutor tutor) {
        return new TutorEntity(
                tutor.getId(),
                tutor.getNombre(),
                String.join(SEPARADOR, tutor.getMaterias()),
                String.join(SEPARADOR, tutor.getHorarios()),
                tutor.getNivel().valor(),
                tutor.getCalificacion(),
                tutor.getModalidad());
    }

    private static List<String> separar(String valor) {
        if (valor == null || valor.isBlank()) {
            return List.of();
        }
        return Arrays.stream(valor.split(SEPARADOR)).map(String::trim).toList();
    }
}
