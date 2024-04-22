package com.api.sportanalytics.service;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
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

            @SuppressWarnings("null")
            byte[] jsonBytes = result.getBytes(StandardCharsets.UTF_8);
            JSONObject obj = new JSONObject(new String(jsonBytes, StandardCharsets.UTF_8));
            String generatedText = obj.getJSONArray("generated_text").getString(0);
            JSONObject generatedTextObj = new JSONObject(generatedText);
            String objetivoDistancia = (String) inputJson.get("objetivoDistancia");
            //String objetivoDistancia = generatedTextObj.getString("objetivoDistancia");
            
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
            Rutina rutina = new Rutina();
            Optional<Atleta> a = atletaService.obtenerAtletaPorId(atletaId);
            rutina.setAtleta(a.get());
            rutina.setEstado("pendiente");
            rutina.setFecha(LocalDate.now());
            rutinaRepository.save(rutina);

            for (int i = 0; i < ejerciciosArray.length(); i++) {
                JSONObject ejercicioObj = ejerciciosArray.getJSONObject(i);
                Ejercicio ejercicio = new Ejercicio();
            
                String nombre = ejercicioObj.isNull("nombre") ? null : ejercicioObj.getString("nombre");
                ejercicio.setNombre(nombre);
            
                String descripcion = ejercicioObj.isNull("descripcion") ? null : ejercicioObj.getString("descripcion");
                ejercicio.setDescripcion(descripcion);
            
                String tipo = ejercicioObj.isNull("tipo") ? null : ejercicioObj.getString("tipo");
                ejercicio.setTipo(tipo);
            
                ejercicioRepository.save(ejercicio);
            
                if (!ejercicioObj.isNull("descanso") && !ejercicioObj.isNull("intensidad") && !ejercicioObj.isNull("series")) {
                    Rutina_Ejercicio rutinaEjercicio = new Rutina_Ejercicio();
                    rutinaEjercicio.setEjercicio(ejercicio);
                    rutinaEjercicio.setRutina(rutina);
            
                    String descanso = ejercicioObj.isNull("descanso") ? null : ejercicioObj.getString("descanso");
                    int cantidadSeries = ejercicioObj.isNull("series") ? 0 : ejercicioObj.getInt("series");
                    String intensidad = ejercicioObj.isNull("intensidad") ? null : ejercicioObj.getString("intensidad");
            
                    rutinaEjercicio.setDescanso(descanso);
                    rutinaEjercicio.setCantidad_series(cantidadSeries);
                    rutinaEjercicio.setIntensidad(intensidad);
                    rutinaEjercicioRepository.save(rutinaEjercicio);
                }
            }
            return finalObj.toString();
        } else {
            throw new RuntimeException(String.format("Error from LLM: %s", response.toString()));
        }
    }
}