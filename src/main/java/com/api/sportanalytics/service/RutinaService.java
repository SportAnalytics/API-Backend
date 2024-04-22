package com.api.sportanalytics.service;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import com.api.sportanalytics.model.Atleta;
import com.api.sportanalytics.model.Ejercicio;
import com.api.sportanalytics.model.Rutina;
import com.api.sportanalytics.model.Rutina_Ejercicio;
import com.api.sportanalytics.repository.EjercicioRepository;
import com.api.sportanalytics.repository.RutinaEjercicioRepository;
import com.api.sportanalytics.repository.RutinaRepository;
import com.api.sportanalytics.shared.exception.ResourceNotFoundException;

import org.json.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@Service
public class RutinaService {
    @Autowired
    private EjercicioRepository ejercicioRepository;

    @Autowired
    private RutinaEjercicioRepository rutinaEjercicioRepository;

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private AtletaService atletaService;
    private final Logger log = LoggerFactory.getLogger(getClass());


    @Transactional
    public String generateRutina(Map<String, Object> inputJson, Long atletaId) {
        String url = "https://smashing-kangaroo-electric.ngrok-free.app/model_inference";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(inputJson, headers);        
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
  
        
        if(response.getStatusCode() == HttpStatus.OK) {
            String result = response.getBody();
            log.info("RUTINA:\n {}", result);

            byte[] jsonBytes = result.getBytes(StandardCharsets.UTF_8);
            JSONObject obj = new JSONObject(new String(jsonBytes, StandardCharsets.UTF_8));
            String generatedText = obj.getJSONArray("generated_text").getString(0);
            JSONObject generatedTextObj = new JSONObject(generatedText);
            String objetivoDistancia = (String) inputJson.get("objetivoDistancia");
            
            JSONArray ejercicios = new JSONArray();

            ejercicios.put(new JSONObject()
            .put("nombre", "calentamiento")
            .put("descripcion", generatedTextObj.getString("calentamiento"))
            .put("tipo", "calentamiento")
            );
        
            for (int i = 0; i < generatedTextObj.getJSONArray("drills").length(); i++) {
                ejercicios.put(new JSONObject()
                .put("nombre", generatedTextObj.getJSONArray("drills").getJSONObject(i).getString("nombre"))
                .put("descripcion", generatedTextObj.getJSONArray("drills").getJSONObject(i).getString("descripcion"))
                .put("tipo", "drills")
                );
            }
            for (int i = 0; i < generatedTextObj.getJSONArray("sprints").length(); i++) {
                JSONObject sprint = generatedTextObj.getJSONArray("sprints").getJSONObject(i);
                ejercicios.put(new JSONObject()
                .put("nombre", sprint.getString("distancia"))
                .put("series", sprint.getString("series"))
                .put("intensidad", sprint.getString("intensidad"))
                .put("descanso", sprint.getString("descanso"))
                .put("tipo", "sprints")
                );
            }
            ejercicios.put(new JSONObject()
            .put("nombre", "enfriamiento")
            .put("descripcion", generatedTextObj.getString("enfriamiento"))
            .put("tipo", "enfriamiento")
            );
   
            JSONObject finalObj = new JSONObject()
            .put("objetivoDistancia", objetivoDistancia)
            .put("ejercicios", ejercicios);
            
            JSONArray ejerciciosArray = finalObj.getJSONArray("ejercicios");
            rutinaRepository.updateAllByAtletaId(atletaId, "completado");


            Rutina rutina = new Rutina();
            Optional<Atleta> atletaOptional = atletaService.obtenerAtletaPorId(atletaId);
            Atleta atleta = atletaOptional.orElseThrow(() -> new RuntimeException("No se encontró el atleta con el ID proporcionado"));
            rutina.setAtleta(atleta);
            rutina.setEstado("pendiente");
            rutina.setFecha(LocalDate.now());
            rutinaRepository.save(rutina);

            for (int i = 0; i < ejerciciosArray.length(); i++) {
                JSONObject ejercicioObj = ejerciciosArray.getJSONObject(i);
                Ejercicio ejercicio = new Ejercicio();
                
                String nombre = ejercicioObj.isNull("nombre")? null : ejercicioObj.getString("nombre");
                ejercicio.setNombre(nombre);
                
                String descripcion = ejercicioObj.isNull("descripcion")? null : ejercicioObj.getString("descripcion");
                ejercicio.setDescripcion(descripcion);
                
                String tipo = ejercicioObj.isNull("tipo")? null : ejercicioObj.getString("tipo");
                ejercicio.setTipo(tipo);
                
                ejercicioRepository.save(ejercicio);
                
                Rutina_Ejercicio rutinaEjercicio = new Rutina_Ejercicio();
                rutinaEjercicio.setEjercicio(ejercicio);
                rutinaEjercicio.setRutina(rutina);
                
                if (!ejercicioObj.isNull("descanso")) {
                    rutinaEjercicio.setDescanso(ejercicioObj.getString("descanso"));
                } else {
                    rutinaEjercicio.setDescanso(null);
                }
                
                if (!ejercicioObj.isNull("intensidad")) {
                    rutinaEjercicio.setIntensidad(ejercicioObj.getString("intensidad"));
                } else {
                    rutinaEjercicio.setIntensidad(null);
                }
                
                if (!ejercicioObj.isNull("series")) {
                    rutinaEjercicio.setCantidad_series(ejercicioObj.getInt("series"));
                } else {
                    rutinaEjercicio.setCantidad_series(0);
                }
                
                rutinaEjercicioRepository.save(rutinaEjercicio);
            }
            
            return finalObj.toString();
        } else {
            throw new RuntimeException(String.format("Error from LLM: %s", response.toString()));
        }
    }
    public String getLastPendienteRutina(Long atletaId) {
        Rutina rutina = rutinaRepository.findFirstByAtletaIdAndEstadoOrderByFechaDesc(atletaId, "pendiente");
        if (rutina!= null) {
            JSONObject rutinaJson = new JSONObject()
               .put("id", rutina.getId())
               .put("atleta", rutina.getAtleta().getNombre() + " " + rutina.getAtleta().getApellido())
               .put("fecha", rutina.getFecha())
               .put("estado", rutina.getEstado())
               .put("objetivoDistancia", rutina.getAtleta().getCompetencia())
               .put("ejercicios", new JSONArray());
    
            for (Rutina_Ejercicio rutinaEjercicio : rutina.getRutina_ejercicios()) {
                JSONObject ejercicioJson = new JSONObject()
                .put("id", rutinaEjercicio.getEjercicio().getId())
                .put("nombre", rutinaEjercicio.getEjercicio().getNombre())
                .put("tipo", rutinaEjercicio.getEjercicio().getTipo())
                .put("descripcion", rutinaEjercicio.getEjercicio().getDescripcion())
                .put("descanso", rutinaEjercicio.getDescanso())
                .put("intensidad", rutinaEjercicio.getIntensidad());

                if (rutinaEjercicio.getCantidad_series() > 0) {
                    ejercicioJson.put("cantidad_series", rutinaEjercicio.getCantidad_series());
                }
                rutinaJson.getJSONArray("ejercicios").put(ejercicioJson);
            }
    
            return rutinaJson.toString();
        } else {
            throw new ResourceNotFoundException("Rutina no encontrada");
        }
    }
}