package com.hackathon.tutormatch.web.dto;

public record AuthResponse(UsuarioResponse user, String token) {
}
