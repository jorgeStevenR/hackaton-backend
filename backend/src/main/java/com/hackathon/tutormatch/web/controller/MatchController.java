package com.hackathon.tutormatch.web.controller;

import com.hackathon.tutormatch.application.usecase.BuscarTutorIdealUseCase;
import com.hackathon.tutormatch.web.dto.MatchResponse;
import com.hackathon.tutormatch.web.dto.SolicitudRequest;
import com.hackathon.tutormatch.web.mapper.WebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
@Tag(name = "Matching", description = "Recomendacion del tutor ideal")
public class MatchController {

    private final BuscarTutorIdealUseCase buscarTutorIdeal;
    private final WebMapper mapper;

    @PostMapping
    @Operation(summary = "Calcula el ranking de tutores para una solicitud",
            description = "Ordenado de mayor a menor score; el primero tiene recomendado = true. "
                    + "Lista vacia si ningun tutor domina la materia.")
    @ApiResponse(responseCode = "200", description = "Ranking de tutores (puede estar vacio)")
    @ApiResponse(responseCode = "400", description = "Campos invalidos")
    @ApiResponse(responseCode = "422", description = "Viola una regla de negocio (ej. horario con formato invalido)")
    public List<MatchResponse> match(@Valid @RequestBody SolicitudRequest request) {
        return buscarTutorIdeal.buscar(mapper.aSolicitud(request)).stream()
                .map(mapper::aMatchResponse)
                .toList();
    }
}
