package com.hackathon.tutormatch.web.controller;

import com.hackathon.tutormatch.application.usecase.IniciarSesionUseCase;
import com.hackathon.tutormatch.application.usecase.ObtenerUsuarioActualUseCase;
import com.hackathon.tutormatch.application.usecase.RegistrarUsuarioUseCase;
import com.hackathon.tutormatch.domain.exception.SesionExpiradaException;
import com.hackathon.tutormatch.web.dto.AuthResponse;
import com.hackathon.tutormatch.web.dto.LoginRequest;
import com.hackathon.tutormatch.web.dto.RegistroRequest;
import com.hackathon.tutormatch.web.dto.UsuarioActualResponse;
import com.hackathon.tutormatch.web.mapper.WebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Registro, inicio de sesión y usuario actual (JWT)")
public class AuthController {

    private static final String PREFIJO_BEARER = "Bearer ";

    private final RegistrarUsuarioUseCase registrarUsuario;
    private final IniciarSesionUseCase iniciarSesion;
    private final ObtenerUsuarioActualUseCase obtenerUsuarioActual;
    private final WebMapper mapper;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crea una cuenta y deja la sesión iniciada")
    @ApiResponse(responseCode = "201", description = "Usuario creado, devuelve { user, token }")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "409", description = "Ya existe una cuenta con ese correo")
    public AuthResponse registrar(@Valid @RequestBody RegistroRequest request) {
        return mapper.aAuthResponse(registrarUsuario.registrar(request.name(), request.email(), request.password()));
    }

    @PostMapping("/login")
    @Operation(summary = "Inicia sesión", description = "rememberMe = true da un token de 30 días; si no, de 1 día.")
    @ApiResponse(responseCode = "200", description = "Devuelve { user, token }")
    @ApiResponse(responseCode = "401", description = "Correo o contraseña incorrectos")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        boolean recordar = Boolean.TRUE.equals(request.rememberMe());
        return mapper.aAuthResponse(iniciarSesion.iniciarSesion(request.email(), request.password(), recordar));
    }

    @GetMapping("/me")
    @Operation(summary = "Usuario dueño del token", description = "Encabezado: Authorization: Bearer <token>")
    @ApiResponse(responseCode = "200", description = "Devuelve { user }")
    @ApiResponse(responseCode = "401", description = "Sesión expirada o token inválido")
    public UsuarioActualResponse me(
            @Parameter(description = "Bearer <token>")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization) {
        if (authorization == null || !authorization.startsWith(PREFIJO_BEARER)) {
            throw new SesionExpiradaException();
        }
        String token = authorization.substring(PREFIJO_BEARER.length()).trim();
        return new UsuarioActualResponse(mapper.aUsuarioResponse(obtenerUsuarioActual.obtenerPorToken(token)));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cierra sesión", description = "JWT sin estado: basta con que el frontend borre el token.")
    @ApiResponse(responseCode = "204", description = "Sesión cerrada")
    public void logout() {
        // Sin estado en el servidor: no hay nada que invalidar
    }
}
