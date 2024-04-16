package com.api.sportanalytics.service;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Collections;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.json.*;
import java.nio.charset.StandardCharsets;

@Service
public class RutinaService {
  public String generateRutina(String inputText) {
    String url = "https://740d-34-125-80-45.ngrok-free.app/model_inference";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_PLAIN);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<String> entity = new HttpEntity<>(inputText, headers);
    
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
    if(response.getStatusCode() == HttpStatus.OK) {

      String result = response.getBody();

      // Creamos un array de bytes a partir de la cadena de texto utilizando la codificación UTF-8
      @SuppressWarnings("null")
      byte[] jsonBytes = result.getBytes(StandardCharsets.UTF_8);

      // Creamos un nuevo objeto JSON a partir del array de bytes
      JSONObject obj = new JSONObject(new String(jsonBytes, StandardCharsets.UTF_8));

      // Extraemos el primer y único elemento del array "generated_text"
      String generatedText = obj.getJSONArray("generated_text").getString(0);
      JSONObject generatedTextObj = new JSONObject(generatedText);

      // Extraemos la propiedad "objetivoDistancia" del primer objeto
      String objetivoDistancia = generatedTextObj.getString("objetivoDistancia");

      // Creamos el array de "ejercicios"
      JSONArray ejercicios = new JSONArray();


      // Añadimos el objeto "calentamiento"
      ejercicios.put(new JSONObject()
          .put("nombre", "calentamiento")
          .put("descripcion", generatedTextObj.getString("calentamiento"))
          .put("tipo", "calentamiento")
      );

      // Añadimos los objetos "drills"
      for (int i = 0; i < generatedTextObj.getJSONArray("drills").length(); i++) {
          ejercicios.put(new JSONObject()
              .put("nombre", generatedTextObj.getJSONArray("drills").getJSONObject(i).getString("nombre"))
              .put("descripcion", generatedTextObj.getJSONArray("drills").getJSONObject(i).getString("descripcion"))
              .put("tipo", "drills")
          );
      }

      // Añadimos los objetos "sprints"
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

      // Añadimos el objeto "enfriamiento"
      ejercicios.put(new JSONObject()
          .put("nombre", "enfriamiento")
          .put("descripcion", generatedTextObj.getString("enfriamiento"))
          .put("tipo", "enfriamiento")
      );

      // Añadimos el array de "ejercicios" al objeto final
      JSONObject finalObj = new JSONObject()
          .put("objetivoDistancia", objetivoDistancia)
          .put("ejercicios", ejercicios);

      return finalObj.toString();
    } else {
      throw new RuntimeException(String.format("Error from LLM: %s", response.toString()));
    }
  }
}