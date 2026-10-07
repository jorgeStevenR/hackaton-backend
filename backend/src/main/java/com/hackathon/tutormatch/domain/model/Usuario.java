package com.hackathon.tutormatch.domain.model;

import com.hackathon.tutormatch.domain.exception.DatosInvalidosException;

import java.util.Locale;
import java.util.regex.Pattern;

/** Usuario de la plataforma. Guarda solo el hash de la contrasena, nunca la contrasena en claro. */
public class Usuario {

    private static final Pattern FORMATO_EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    /** Minimo 8 caracteres, con al menos una mayuscula, una minuscula y un numero. */
    private static final Pattern PASSWORD_SEGURA = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    private final Long id;
    private final String nombre;
    private final String email;
    private final String passwordHash;

    public Usuario(Long id, String nombre, String email, String passwordHash) {
        if (Textos.estaVacio(nombre) || nombre.trim().length() < 2) {
            throw new DatosInvalidosException("El nombre debe tener al menos 2 caracteres");
        }
        if (Textos.estaVacio(passwordHash)) {
            throw new DatosInvalidosException("La contrasena es obligatoria");
        }
        this.id = id;
        this.nombre = nombre.trim();
        this.email = normalizarEmail(email);
        this.passwordHash = passwordHash;
    }

    /** Minusculas y sin espacios; falla si el formato no es valido. */
    public static String normalizarEmail(String email) {
        String normalizado = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        if (!FORMATO_EMAIL.matcher(normalizado).matches()) {
            throw new DatosInvalidosException("El correo no tiene un formato valido");
        }
        return normalizado;
    }

    /** Valida la contrasena en claro antes de cifrarla. */
    public static void validarPassword(String password) {
        if (password == null || !PASSWORD_SEGURA.matcher(password).matches()) {
            throw new DatosInvalidosException(
                    "La contraseña debe tener mínimo 8 caracteres, con al menos una mayúscula, una minúscula y un número");
        }
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
