package com.petsalud.backendpetsalud.service;

import com.petsalud.backendpetsalud.entity.Usuario;
import com.petsalud.backendpetsalud.entity.Rol;
import com.petsalud.backendpetsalud.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario registrarUsuario(Usuario usuario) {

        // Validar que el email esté informado
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        // Validar que la contraseña esté informada
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        // Verificar si el email ya está registrado
        Optional<Usuario> usuarioExistente =
                usuarioRepository.findByEmail(usuario.getEmail());

        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Nunca guardar la contraseña en texto plano
        usuario.setPassword(
                passwordEncoder.encode(usuario.getPassword())
        );

        // El registro público siempre crea usuarios CLIENTE
        usuario.setRol(Rol.CLIENTE);

        // Evitar que el registro asigne un ID enviado por el cliente
        usuario.setId(null);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(usuarioActualizado.getNombre());
            usuario.setApellido(usuarioActualizado.getApellido());
            usuario.setTelefono(usuarioActualizado.getTelefono());
            usuario.setDireccion(usuarioActualizado.getDireccion());

            // No actualizar email, password ni rol aquí
            return usuarioRepository.save(usuario);
        }).orElseThrow(
                () -> new IllegalArgumentException("Usuario no encontrado")
        );
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
}