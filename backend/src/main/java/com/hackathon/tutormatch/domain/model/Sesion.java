package com.hackathon.tutormatch.domain.model;

/** Usuario autenticado junto con su token de acceso. */
public record Sesion(Usuario usuario, String token) {
}
