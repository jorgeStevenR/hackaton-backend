package com.hackathon.tutormatch.web.controller;

import com.hackathon.tutormatch.application.usecase.ListarSolicitudesUseCase;
import com.hackathon.tutormatch.web.dto.SolicitudResponse;
import com.hackathon.tutormatch.web.mapper.WebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
@Tag(name = "Solicitudes", description = "Historial de solicitudes y asignaciones (se registran en POST /api/match)")
public class SolicitudController {

    private final ListarSolicitudesUseCase listarSolicitudes;
    private final WebMapper mapper;

    @GetMapping
    @Operation(summary = "Historial de solicitudes con el tutor asignado", description = "Más recientes primero.")
    @ApiResponse(responseCode = "200", description = "Lista de solicitudes registradas")
    public List<SolicitudResponse> listar() {
        return listarSolicitudes.listar().stream().map(mapper::aSolicitudResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Una solicitud registrada")
    @ApiResponse(responseCode = "200", description = "Solicitud encontrada")
    @ApiResponse(responseCode = "404", description = "No existe una solicitud con ese id")
    public SolicitudResponse obtener(@PathVariable Long id) {
        return mapper.aSolicitudResponse(listarSolicitudes.obtenerPorId(id));
    }
}
