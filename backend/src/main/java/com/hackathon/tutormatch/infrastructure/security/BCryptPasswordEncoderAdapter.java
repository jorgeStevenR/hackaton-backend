package com.hackathon.tutormatch.infrastructure.security;

import com.hackathon.tutormatch.domain.port.PasswordEncoderPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoderAdapter implements PasswordEncoderPort {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String cifrar(String passwordEnClaro) {
        return encoder.encode(passwordEnClaro);
    }

    @Override
    public boolean coincide(String passwordEnClaro, String passwordHash) {
        return encoder.matches(passwordEnClaro, passwordHash);
    }
}
