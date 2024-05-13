package com.api.sportanalytics.service;
import com.api.sportanalytics.model.*;
import com.api.sportanalytics.repository.SerieRepository;
import com.api.sportanalytics.request.UpdateRutinaRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import com.api.sportanalytics.repository.EjercicioRepository;
import com.api.sportanalytics.repository.RutinaEjercicioRepository;
import com.api.sportanalytics.repository.RutinaRepository;
import com.api.sportanalytics.shared.exception.ResourceNotFoundException;
import com.api.sportanalytics.shared.exception.ResourceValidationException;
import org.json.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RutinaService {
    @Autowired
    private EjercicioRepository ejercicioRepository;

    @Autowired
    private RutinaEjercicioRepository rutinaEjercicioRepository;

    @Autowired
    private SerieRepository serieRepository;
    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private AtletaService atletaService;
    private final Logger log = LoggerFactory.getLogger(getClass());


    @Transactional
    public String generateRutina(Map<String, Object> completeJson, Long atletaId) {
        Map<String, Object> inputJson = (Map<String, Object>) completeJson.get("inputs");
        String objetivoDistancia = (String) inputJson.get("objetivoDistancia");
        if (objetivoDistancia == null) {
            throw new ResourceValidationException("El atleta debe proporcionar un objetivo de distancia.");
        }
         String url = "https://smashing-kangaroo-electric.ngrok-free.app/model_inference";
      // String url = "https://kjnafpjozk85a3gw.us-east-1.aws.endpoints.huggingface.cloud";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(completeJson, headers);        
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
  
        
        if(response.getStatusCode() == HttpStatus.OK) {
            String result = response.getBody();
            log.info("RUTINA:\n {}", result);

            byte[] jsonBytes = result.getBytes(StandardCharsets.UTF_8);
            JSONObject obj = new JSONObject(new String(jsonBytes, StandardCharsets.UTF_8));
            String generatedText = obj.getJSONArray("generated_text").getString(0);
            JSONObject generatedTextObj = new JSONObject(generatedText);


            JSONArray ejercicios = new JSONArray();

            ejercicios.put(new JSONObject()
            .put("nombre", "calentamiento")
            .put("descripcion", generatedTextObj.optString("calentamiento", null))
            .put("tipo", "calentamiento")
            );
        

            JSONArray drillsArray = generatedTextObj.optJSONArray("drills");
            if (drillsArray!= null) {
                for (int i = 0; i < drillsArray.length(); i++) {
                    JSONObject drill = drillsArray.getJSONObject(i);
                    ejercicios.put(new JSONObject()
                    .put("nombre", drill.optString("nombre", null))
                    .put("descripcion", drill.optString("descripcion", null))
                    .put("tipo", "drills")
                    );
                }
            }


            JSONArray sprintsArray = generatedTextObj.optJSONArray("sprints");
            if (sprintsArray!= null) {
                for (int i = 0; i < sprintsArray.length(); i++) {
                    JSONObject sprint = sprintsArray.getJSONObject(i);
                    ejercicios.put(new JSONObject()
                    .put("nombre", sprint.optString("distancia", null))
                    .put("series", sprint.optString("series", null))
                    .put("intensidad", sprint.optString("intensidad", null))
                    .put("descanso", sprint.optString("descanso", null))
                    .put("tipo", "sprints")
                    );
                }
            }
            ejercicios.put(new JSONObject()
            .put("nombre", "enfriamiento")
            .put("descripcion", generatedTextObj.optString("enfriamiento", null))
            .put("tipo", "enfriamiento")
            );
   
            JSONObject finalObj = new JSONObject()
            .put("objetivoDistancia", objetivoDistancia)
            .put("ejercicios", ejercicios);
            
            JSONArray ejerciciosArray = finalObj.getJSONArray("ejercicios");
            rutinaRepository.updateAllByAtletaId(atletaId, "completado");

            Rutina rutina = new Rutina();
            Optional<Atleta> atletaOptional = atletaService.obtenerAtletaPorId(atletaId);
            Atleta atleta = atletaOptional.orElseThrow(() -> new ResourceNotFoundException("No se encontró el atleta con el ID proporcionado"));
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
            throw new ResourceValidationException(String.format("Error from LLM: %s", response.toString()));
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
    public List<Rutina_Ejercicio> getEjerciciosByAtletaAndNombre(Long atletaId, String nombre) {
        List<Rutina_Ejercicio> ejercicios = rutinaEjercicioRepository.findByRutinaAtletaIdAndEjercicioNombre(atletaId, nombre);
        if (ejercicios.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron ejercicios con el nombre '" + nombre + "'");
        }

        return ejercicios;
    
        /*JSONObject jsonObject = new JSONObject();
        jsonObject.put("ejercicios", new JSONArray());
    
        for (Rutina_Ejercicio ejercicio : ejercicios) {
            JSONObject ejercicioJson = new JSONObject()
                   .put("id", ejercicio.getEjercicio().getId())
                   .put("nombre", ejercicio.getEjercicio().getNombre())
                   .put("tipo", ejercicio.getEjercicio().getTipo())
                   .put("descripcion", ejercicio.getEjercicio().getDescripcion())
                   .put("descanso", ejercicio.getDescanso())
                   .put("intensidad", ejercicio.getIntensidad())
                   .put("cantidad_series", ejercicio.getCantidad_series());
    
            jsonObject.getJSONArray("ejercicios").put(ejercicioJson);
        }*/
    
        //return jsonObject.toString();
    }

    public List<Rutina> getAllRutinesByUserId( Long ateltaId) {
        return rutinaRepository.findRutinaByAtletaIdOrderByIdDesc(ateltaId);
    }

    public ResponseEntity<Rutina> updateRUtina(UpdateRutinaRequest request) {

        Optional<Rutina> empRUtina = rutinaRepository.findById(request.getId());
        if (empRUtina.isEmpty()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Rutina rutinaFinal = empRUtina.get();
        rutinaFinal.setHumedad_promedio(request.getHumedad());
        rutinaFinal.setPresion_promedio(request.getPresion());
        rutinaFinal.setTemperatura_promedio(request.getTemperatura());
        rutinaFinal.setEstado("Completa");
        rutinaFinal.setFecha_completada(LocalDate.now());


        for (int i = 0; i< rutinaFinal.getRutina_ejercicios().size(); i++ ) {


            if (rutinaFinal.getRutina_ejercicios().get(i).getCantidad_series() == 0) {
                for (int j = 0; j< 1; j++ ) {
                    Serie temp = Serie.builder()
                            .velocidad(request.getVelocidad().get(i))
                            .fre_cardiaca(request.getFrecuenciaCardiaca().get(i))
                            .tiempo(request.getTiempo().get(i))
                            .rutina_ejercicio(rutinaFinal.getRutina_ejercicios().get(i)).
                            // .numero_serie("0").
                                    build();
                    serieRepository.save(temp);
                    List<Serie> listTemp = new ArrayList<>();
                    listTemp.add(temp);
                    rutinaFinal.getRutina_ejercicios().get(i).setSeries(listTemp);

                }
            } else {
                List<Serie> listTemp = new ArrayList<>();
                for (int j = 0; j< rutinaFinal.getRutina_ejercicios().get(i).getCantidad_series(); j++ ) {
                    Serie temp = Serie.builder()
                            .velocidad(request.getVelocidad().get(i))
                            .fre_cardiaca(request.getFrecuenciaCardiaca().get(i))
                            .tiempo(request.getTiempo().get(i))
                            .rutina_ejercicio(rutinaFinal.getRutina_ejercicios().get(i)).
                            //.numero_serie("0").
                                    build();
                    serieRepository.save(temp);
                    listTemp.add(temp);


                }
                rutinaFinal.getRutina_ejercicios().get(i).setSeries(listTemp);
            }



        }


        rutinaRepository.save(rutinaFinal);
        return ResponseEntity.status(HttpStatus.OK).body(rutinaFinal);
    }

    public List<String> getAllEjercicios( Long ateltaId) {
        return rutinaEjercicioRepository.findDistinctExerciseNamesByAtletaId(ateltaId);
    }

}