package com.api.sportanalytics.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.sportanalytics.model.Atleta;
import com.api.sportanalytics.service.AtletaService;
import com.api.sportanalytics.shared.exception.ResourceNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/atletas")
public class AtletaController {
    
    private final AtletaService atletaService;

    public AtletaController(AtletaService atletaService) {
        this.atletaService = atletaService;
    }

    // Endpoint para crear un nuevo atleta
    @PostMapping
    public ResponseEntity<Atleta> crearAtleta(@Valid @RequestBody Atleta atleta) {
        Atleta nuevoAtleta = atletaService.crearAtleta(atleta);
        return new ResponseEntity<>(nuevoAtleta, HttpStatus.CREATED);
    }

    // Endpoint para obtener todos los atletas
    @GetMapping
    public ResponseEntity<List<Atleta>> obtenerTodosLosAtletas() {
        List<Atleta> atletas = atletaService.obtenerTodosLosAtletas();
        return new ResponseEntity<>(atletas, HttpStatus.OK);
    }

    // Endpoint para obtener un atleta por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Atleta> obtenerAtletaPorId(@PathVariable Long id) {
        Atleta atleta = atletaService.obtenerAtletaPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atleta", id));
        return ResponseEntity.ok(atleta);
    }

    
    // Endpoint para actualizar un atleta por su ID
    @PutMapping("/{id}")
    public ResponseEntity<Atleta> actualizarAtleta(@PathVariable Long id, @RequestBody Atleta atletaActualizar) {
        Atleta atletaActualizado = atletaService.actualizarAtleta(id, atletaActualizar);
        return ResponseEntity.ok(atletaActualizado);
    }

    // Endpoint para eliminar un atleta por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarAtleta(@PathVariable Long id) {
        Atleta atleta = atletaService.obtenerAtletaPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atleta", id));
        atletaService.eliminarAtleta(id);
        String mensaje = String.format("El atleta %s con id %d fue eliminado", atleta.getNombre(), id);
        return ResponseEntity.ok(mensaje);
    }
}
