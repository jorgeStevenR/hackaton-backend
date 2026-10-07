package com.hackathon.tutormatch.web.controller;

import com.hackathon.tutormatch.application.usecase.ListarTutoresUseCase;
import com.hackathon.tutormatch.application.usecase.RegistrarTutorUseCase;
import com.hackathon.tutormatch.web.dto.TutorRequest;
import com.hackathon.tutormatch.web.dto.TutorResponse;
import com.hackathon.tutormatch.web.mapper.WebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tutores")
@RequiredArgsConstructor
@Tag(name = "Tutores", description = "Registro y consulta de tutores")
public class TutorController {

    private final ListarTutoresUseCase listarTutores;
    private final RegistrarTutorUseCase registrarTutor;
    private final WebMapper mapper;

    @GetMapping
    @Operation(summary = "Lista todos los tutores")
    @ApiResponse(responseCode = "200", description = "Lista de tutores")
    public List<TutorResponse> listar() {
        return listarTutores.listar().stream().map(mapper::aTutorResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un tutor por id")
    @ApiResponse(responseCode = "200", description = "Tutor encontrado")
    @ApiResponse(responseCode = "404", description = "No existe un tutor con ese id")
    public TutorResponse obtener(@PathVariable Long id) {
        return mapper.aTutorResponse(listarTutores.obtenerPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registra un tutor")
    @ApiResponse(responseCode = "201", description = "Tutor registrado")
    @ApiResponse(responseCode = "400", description = "Campos invalidos")
    @ApiResponse(responseCode = "422", description = "Viola una regla de negocio (ej. horario con formato invalido)")
    public TutorResponse registrar(@Valid @RequestBody TutorRequest request) {
        return mapper.aTutorResponse(registrarTutor.registrar(mapper.aTutor(request)));
    }
}
