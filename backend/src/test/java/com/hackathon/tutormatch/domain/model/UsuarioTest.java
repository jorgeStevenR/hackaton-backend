package com.hackathon.tutormatch.domain.model;

import com.hackathon.tutormatch.domain.exception.DatosInvalidosException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsuarioTest {

    @Test
    @DisplayName("El email se guarda en minusculas y sin espacios")
    void emailNormalizado() {
        Usuario usuario = new Usuario(null, "Ada", "  ADA@Correo.com ", "hash");

        assertThat(usuario.getEmail()).isEqualTo("ada@correo.com");
    }

    @Test
    @DisplayName("Email con formato invalido es rechazado")
    void emailInvalido() {
        assertThatThrownBy(() -> new Usuario(null, "Ada", "no-es-correo", "hash"))
                .isInstanceOf(DatosInvalidosException.class);
    }

    @Test
    @DisplayName("Nombre de menos de 2 caracteres es rechazado")
    void nombreCorto() {
        assertThatThrownBy(() -> new Usuario(null, " A ", "ada@correo.com", "hash"))
                .isInstanceOf(DatosInvalidosException.class);
    }

    @Test
    @DisplayName("Contrasena segura es aceptada")
    void passwordSegura() {
        assertThatCode(() -> Usuario.validarPassword("Demo1234")).doesNotThrowAnyException();
    }

    @ParameterizedTest(name = "\"{0}\" es rechazada")
    @ValueSource(strings = {"Dem1234", "demo1234", "DEMO1234", "DemoDemo"})
    @DisplayName("Contrasena debil es rechazada")
    void passwordDebil(String password) {
        assertThatThrownBy(() -> Usuario.validarPassword(password))
                .isInstanceOf(DatosInvalidosException.class);
    }
}
