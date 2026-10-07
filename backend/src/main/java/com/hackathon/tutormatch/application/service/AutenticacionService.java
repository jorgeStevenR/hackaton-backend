package com.hackathon.tutormatch.application.service;

import com.hackathon.tutormatch.application.usecase.IniciarSesionUseCase;
import com.hackathon.tutormatch.application.usecase.ObtenerUsuarioActualUseCase;
import com.hackathon.tutormatch.application.usecase.RegistrarUsuarioUseCase;
import com.hackathon.tutormatch.domain.exception.CredencialesInvalidasException;
import com.hackathon.tutormatch.domain.exception.DatosInvalidosException;
import com.hackathon.tutormatch.domain.exception.EmailYaRegistradoException;
import com.hackathon.tutormatch.domain.exception.SesionExpiradaException;
import com.hackathon.tutormatch.domain.model.Sesion;
import com.hackathon.tutormatch.domain.model.Usuario;
import com.hackathon.tutormatch.domain.port.PasswordEncoderPort;
import com.hackathon.tutormatch.domain.port.TokenProviderPort;
import com.hackathon.tutormatch.domain.port.UsuarioRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutenticacionService implements RegistrarUsuarioUseCase, IniciarSesionUseCase, ObtenerUsuarioActualUseCase {

    private static final Logger log = LoggerFactory.getLogger(AutenticacionService.class);

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenProviderPort tokenProvider;

    public AutenticacionService(UsuarioRepositoryPort usuarioRepository, PasswordEncoderPort passwordEncoder,
                                TokenProviderPort tokenProvider) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    @Transactional
    public Sesion registrar(String nombre, String email, String password) {
        String emailNormalizado = Usuario.normalizarEmail(email);
        Usuario.validarPassword(password);
        if (usuarioRepository.existePorEmail(emailNormalizado)) {
            throw new EmailYaRegistradoException();
        }
        Usuario usuario = usuarioRepository.guardar(
                new Usuario(null, nombre, emailNormalizado, passwordEncoder.cifrar(password)));
        log.info("Usuario registrado id={} email={}", usuario.getId(), usuario.getEmail());
        return new Sesion(usuario, tokenProvider.generar(usuario, false));
    }

    @Override
    @Transactional(readOnly = true)
    public Sesion iniciarSesion(String email, String password, boolean recordarSesion) {
        String emailNormalizado;
        try {
            emailNormalizado = Usuario.normalizarEmail(email);
        } catch (DatosInvalidosException e) {
            throw new CredencialesInvalidasException();
        }
        Usuario usuario = usuarioRepository.buscarPorEmail(emailNormalizado)
                .filter(u -> password != null && passwordEncoder.coincide(password, u.getPasswordHash()))
                .orElseThrow(CredencialesInvalidasException::new);
        log.info("Inicio de sesion id={} recordar={}", usuario.getId(), recordarSesion);
        return new Sesion(usuario, tokenProvider.generar(usuario, recordarSesion));
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorToken(String token) {
        return tokenProvider.validar(token)
                .flatMap(usuarioRepository::buscarPorId)
                .orElseThrow(SesionExpiradaException::new);
    }
}
