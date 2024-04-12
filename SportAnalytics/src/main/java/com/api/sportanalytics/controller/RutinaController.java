package com.api.sportanalytics.controller;

import com.api.sportanalytics.model.Perfil;
import com.api.sportanalytics.service.AtletaService;
import com.api.sportanalytics.service.RutinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/rutinas")
public class RutinaController
{
    @Autowired
    private RutinaService rutinaService;

    @PostMapping
    public String generarRutina() throws IOException {
        return rutinaService.generarRutina();
    }
}
