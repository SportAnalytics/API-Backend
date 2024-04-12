package com.api.sportanalytics.service;

import com.api.sportanalytics.model.Atleta;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class RutinaService {

    public String generarRutina() throws IOException {
        // URL de la API a la que deseas llamar
        String apiUrl = "https://ba32-34-141-198-193.ngrok-free.app/model_inference";

        // Crear la conexión HTTP
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Establecer el método de solicitud (POST en este caso)
        connection.setRequestMethod("POST");

        // Configurar los encabezados de la solicitud
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        // Habilitar el envío de datos (true para POST, PUT, DELETE, etc.)
        connection.setDoOutput(true);

        // Crear el cuerpo de la solicitud JSON
        String jsonInputString = "{\"inputs\": \"[INST]<<SYS>>\\nA continuación se presentan parámetros fisiológicos y ambientales capturados por los sensores de un smartwatch utilizado por un atleta. Basándote en estos datos y en tu conocimiento de las rutinas de entrenamiento para atletas, crea una rutina diaria de entrenamiento personalizada para el atleta.\\n<</SYS>>\\n\\n {\\\"objetivoDistancia\\\": \\\"100m\\\", \\\"presionAmbiental\\\": 1020, \\\"humedadAmbiental\\\": 70, \\\"temperaturaAmbiental\\\": 26, \\\"Calentamiento\\\": {\\\"tiempo\\\": \\\"00:18:00\\\", \\\"ejercicio\\\": \\\"600m de trote rápido\\\"}, \\\"DRILLS\\\": {\\\"tiempo\\\": \\\"00:15:00\\\", \\\"ejercicios\\\": [\\\"Sprints explosivos\\\", \\\"Ejercicios de reacción\\\", \\\"Cambio de dirección\\\", \\\"Saltos de altura\\\", \\\"Carreras de obstáculos\\\"]}, \\\"sprints\\\": [{\\\"distancia\\\": \\\"40m\\\", \\\"series\\\": \\\"8\\\", \\\"intensidad\\\": \\\"100%\\\", \\\"descanso\\\": \\\"1 minuto\\\", \\\"frecuenciaCardiaca\\\": [192, 194, 196, 198, 200, 202, 204, 206], \\\"promedioFrecuenciaCardiaca\\\": 197, \\\"velocidad\\\": [8.4, 8.6, 8.8, 9.0, 9.2, 9.4, 9.6, 9.8], \\\"promedioVelocidad\\\": 9.1, \\\"tiempo\\\": [\\\"00:00:15\\\", \\\"00:00:15\\\", \\\"00:00:15\\\", \\\"00:00:15\\\", \\\"00:00:15\\\", \\\"00:00:15\\\", \\\"00:00:15\\\", \\\"00:00:15\\\"]}, {\\\"distancia\\\": \\\"60m\\\", \\\"series\\\": \\\"6\\\", \\\"intensidad\\\": \\\"95%\\\", \\\"descanso\\\": \\\"2 minutos\\\", \\\"frecuenciaCardiaca\\\": [187, 189, 191, 193, 195, 197], \\\"promedioFrecuenciaCardiaca\\\": 192, \\\"velocidad\\\": [8.9, 9.1, 9.3, 9.5, 9.7, 9.9], \\\"promedioVelocidad\\\": 9.4, \\\"tiempo\\\": [\\\"00:00:20\\\", \\\"00:00:20\\\", \\\"00:00:20\\\", \\\"00:00:20\\\", \\\"00:00:20\\\", \\\"00:00:20\\\"]}], \\\"Enfriamiento\\\": {\\\"tiempo\\\": \\\"00:17:00\\\", \\\"ejercicio\\\": \\\"Trote de enfriamiento y estiramientos ligeros\\\"}}[/INST]\"}";

        // Enviar los datos JSON
        try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            outputStream.write(jsonInputString.getBytes());
        }

        // Leer la respuesta de la API
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Imprimir la respuesta
        System.out.println("Respuesta de la API: " + response.toString());

        // Cerrar la conexión
        connection.disconnect();

        String res = "Respuesta de la API: " + response.toString();

        return res;
    }

}
