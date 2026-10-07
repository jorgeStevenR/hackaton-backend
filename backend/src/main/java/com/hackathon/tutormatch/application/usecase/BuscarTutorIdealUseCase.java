package com.hackathon.tutormatch.application.usecase;

import com.hackathon.tutormatch.domain.model.ResultadoMatch;
import com.hackathon.tutormatch.domain.model.Solicitud;

import java.util.List;

public interface BuscarTutorIdealUseCase {

    /** Ranking de tutores de mayor a menor afinidad; vacio si nadie domina la materia. */
    List<ResultadoMatch> buscar(Solicitud solicitud);
}
