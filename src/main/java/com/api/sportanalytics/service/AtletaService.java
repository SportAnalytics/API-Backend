package com.api.sportanalytics.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.api.sportanalytics.model.Atleta;
import com.api.sportanalytics.repository.AtletaRepository;
import com.api.sportanalytics.shared.exception.ResourceNotFoundException;

@Service
public class AtletaService {
    
    private final AtletaRepository atletaRepository;

    public AtletaService(AtletaRepository atletaRepository) {
        this.atletaRepository = atletaRepository;
    }

    // crear un nuevo atleta
    public Atleta crearAtleta(Atleta atleta) {
        return atletaRepository.save(atleta);
    }

    // obtener todos los atletas
    public List<Atleta> obtenerTodosLosAtletas() {
        return atletaRepository.findAll();
    }

    // obtener un atleta por su ID
    public Optional<Atleta> obtenerAtletaPorId(Long id) {
        return atletaRepository.findById(id);
    }
    

    // actualizar un atleta
    public Atleta actualizarAtleta(Long id, Atleta atletaActualizar) {
        Atleta atletaExistente = atletaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atleta", id));
    
        atletaExistente.setNombre(atletaActualizar.getNombre());
        atletaExistente.setApellido(atletaActualizar.getApellido());
        atletaExistente.setDireccion(atletaActualizar.getDireccion());
        atletaExistente.setTelefono(atletaActualizar.getTelefono());
        atletaExistente.setCompetencia(atletaActualizar.getCompetencia());
    
        return atletaRepository.save(atletaExistente);
    }

    // eliminar un atleta por su ID
    public void eliminarAtleta(Long id) {
        atletaRepository.deleteById(id);
    }
}
