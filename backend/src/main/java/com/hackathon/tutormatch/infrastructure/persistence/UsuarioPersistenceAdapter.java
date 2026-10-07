package com.hackathon.tutormatch.infrastructure.persistence;

import com.hackathon.tutormatch.domain.model.Usuario;
import com.hackathon.tutormatch.domain.port.UsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {

    private final SpringDataUsuarioRepository repository;

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity guardado = repository.save(new UsuarioEntity(
                usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getPasswordHash()));
        return aDominio(guardado);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id).map(UsuarioPersistenceAdapter::aDominio);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmail(email).map(UsuarioPersistenceAdapter::aDominio);
    }

    @Override
    public boolean existePorEmail(String email) {
        return repository.existsByEmail(email);
    }

    private static Usuario aDominio(UsuarioEntity entity) {
        return new Usuario(entity.getId(), entity.getNombre(), entity.getEmail(), entity.getPasswordHash());
    }
}
