package com.hackathon.tutormatch.domain.port;

public interface PasswordEncoderPort {

    String cifrar(String passwordEnClaro);

    boolean coincide(String passwordEnClaro, String passwordHash);
}
