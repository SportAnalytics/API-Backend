package com.api.sportanalytics.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
@NoArgsConstructor
public class UpdateRutinaRequest {

    private Long id;
    private Float temperatura;
    private Float humedad;
    private Integer presion;

    private List<Float> velocidad;
    private List<Float> tiempo;
    private List<Float> frecuenciaCardiaca;

}
