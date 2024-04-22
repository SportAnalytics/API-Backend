package com.api.sportanalytics.controller;

import com.api.sportanalytics.service.AtletaService;
import com.api.sportanalytics.service.RutinaService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.LinkedHashMap;
@RestController
@RequestMapping("/api/rutinas")
public class RutinaController {
    
    @Autowired
    private RutinaService rutinaService;

    @Autowired
    private AtletaService atletaService;

    @PostMapping(value = "/{atletaId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> generateFromOpenAi(@PathVariable Long atletaId, @RequestBody Map<String, Object> inputJson) {
        String objetivoDistancia = atletaService.getObjetivoDistancia(atletaId);

        if (!inputJson.containsKey("objetivoDistancia")) {
            Map<String, Object> newInputJson = new LinkedHashMap<>();
            newInputJson.put("objetivoDistancia", objetivoDistancia);
            newInputJson.putAll(inputJson);
            inputJson = newInputJson;
        }
        
        try {
            String result = rutinaService.generateRutina(inputJson, atletaId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/atleta/{atletaId}")
    public ResponseEntity<String> getLastPendienteRutina(@PathVariable Long atletaId) {
        try {
            String rutinaJson = rutinaService.getLastPendienteRutina(atletaId);
            return new ResponseEntity<>(rutinaJson, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
