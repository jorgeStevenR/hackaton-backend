package com.hackathon.tutormatch.web.dto;

/** Datos publicos del usuario. Nunca incluye la contrasena. El id va como string. */
public record UsuarioResponse(String id, String name, String email) {
}
