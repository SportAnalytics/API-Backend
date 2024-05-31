package com.api.sportanalytics.controller;
import com.api.sportanalytics.model.Perfil;
import com.api.sportanalytics.request.LoginRequest;
import com.api.sportanalytics.service.PerfilService;
import com.api.sportanalytics.shared.exception.ResourceNotFoundException;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/perfiles")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    // Endpoint para obtener todos los perfiles
    @GetMapping
    public ResponseEntity<List<Perfil>> obtenerTodosLosPerfiles() {
        List<Perfil> perfiles = perfilService.obtenerTodosLosPerfiles();
        return new ResponseEntity<>(perfiles, HttpStatus.OK);
    }

    // Endpoint para obtener un perfil por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Perfil> obtenerPerfilPorId(@PathVariable Long id) {
        Perfil perfil = perfilService.obtenerPerfilPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil", id));
        return ResponseEntity.ok(perfil);
    }

    // Endpoint para actualizar un perfil por su ID
    @PutMapping("/{id}")
    public ResponseEntity<Perfil> actualizarPerfil(@PathVariable Long id, @RequestBody Perfil perfilActualizar) {
        Perfil perfilActualizado = perfilService.actualizarPerfil(id, perfilActualizar);
        return ResponseEntity.ok(perfilActualizado);
    }

    // Endpoint para eliminar un perfil por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPerfil(@PathVariable Long id) {
        perfilService.eliminarPerfil(id);
        String mensaje = String.format("El perfil con id %d fue eliminado", id);
        return ResponseEntity.ok(mensaje);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Perfil> chambiarContrasena(@RequestBody LoginRequest request) {
        Perfil perfil = perfilService.obtenerPerfilPorCorreo(request.getCorreo())
                .orElseThrow(() -> new ResourceNotFoundException( String.format( "Correo: %s not found", request.getCorreo() )));


        return perfilService.cambioContrasena(perfil, request.getContraseña());
    }
}
