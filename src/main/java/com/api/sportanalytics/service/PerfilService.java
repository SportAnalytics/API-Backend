package com.api.sportanalytics.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.api.sportanalytics.model.Perfil;
import com.api.sportanalytics.repository.PerfilRepository;
import com.api.sportanalytics.shared.exception.ResourceNotFoundException;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;

  
    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    // Crear un nuevo perfil
    public Perfil crearPerfil(Perfil perfil) {
        return perfilRepository.save(perfil);
    }

    // Obtener todos los perfiles
    public List<Perfil> obtenerTodosLosPerfiles() {
        return perfilRepository.findAll();
    }

    // Obtener un perfil por su ID
    public Optional<Perfil> obtenerPerfilPorId(Long id) {
        return perfilRepository.findById(id);
    }

    public Optional<Perfil> obtenerPerfilPorCorreo(String correo) {
        return perfilRepository.findByCorreo(correo);
    }



    // Actualizar un perfil
    public Perfil actualizarPerfil(Long id, Perfil perfilActualizar) {
        Perfil perfilExistente = perfilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil", id));

        perfilExistente.setAtleta(perfilActualizar.getAtleta());
        perfilExistente.setCorreo(perfilActualizar.getCorreo());
        perfilExistente.setContraseña(perfilActualizar.getContraseña());

        return perfilRepository.save(perfilExistente);
    }

    public ResponseEntity<Perfil> cambioContrasena(Perfil perfilActualizar, String newContrasena) {



        perfilActualizar.setContraseña(newContrasena);

        perfilRepository.save(perfilActualizar);
        return ResponseEntity.status(HttpStatus.OK).body(perfilActualizar);

    }

    // Eliminar un perfil por su ID
    public void eliminarPerfil(Long id) {
        perfilRepository.deleteById(id);
    }
}