package com.api.sportanalytics.controller;

import com.api.sportanalytics.service.RutinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/rutinas")
public class RutinaController {
    
    @Autowired
    private RutinaService rutinaService;

    @PostMapping(consumes = "text/plain", produces = "application/json")
    public ResponseEntity<String> generateFromOpenAi(@RequestBody String inputText) {
        Long atletaId = 1L;
        try {
            String result = rutinaService.generateRutina(inputText, atletaId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
