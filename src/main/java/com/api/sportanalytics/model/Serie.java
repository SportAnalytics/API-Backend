package com.api.sportanalytics.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
@Data
@Setter
@Getter
@Entity
@Table(name = "Serie")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rutina_ejercicio_id")
    @JsonIgnore
    private Rutina_Ejercicio rutina_ejercicio;


    private Integer numero_serie;
    private Float tiempo;
    private Float velocidad;
    private Float fre_cardiaca;


}