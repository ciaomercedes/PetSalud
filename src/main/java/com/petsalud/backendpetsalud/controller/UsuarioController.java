package com.petsalud.backendpetsalud.controller;

import com.petsalud.backendpetsalud.entity.Usuario;
import com.petsalud.backendpetsalud.service.UsuarioService;
import com.petsalud.backendpetsalud.dto.UsuarioResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(
            @RequestBody Usuario usuario) {

        Usuario nuevoUsuario =
                usuarioService.registrarUsuario(usuario);

        return ResponseEntity.ok(
                UsuarioResponseDTO.desde(nuevoUsuario)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(
            @PathVariable Long id) {

        return usuarioService.obtenerUsuarioPorId(id)
                .map(usuario ->
                        ResponseEntity.ok(
                                UsuarioResponseDTO.desde(usuario)
                        ))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody Usuario usuario) {

        Usuario usuarioActualizado =
                usuarioService.actualizarUsuario(id, usuario);

        return ResponseEntity.ok(
                UsuarioResponseDTO.desde(usuarioActualizado)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @PathVariable Long id) {

        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {

        List<UsuarioResponseDTO> usuarios =
                usuarioService.listarUsuarios()
                        .stream()
                        .map(UsuarioResponseDTO::desde)
                        .toList();

        return ResponseEntity.ok(usuarios);
    }
}