package com.api.sportanalytics.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.math.BigDecimal;
@Data
@Setter
@Getter
@Entity
@Table(name = "Rutina")
public class Rutina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "atleta_id")
    @JsonIgnore
    private Atleta atleta;
    private LocalDate fecha;
    private LocalDate fecha_completada;
    private String estado;

    @OneToMany(mappedBy = "rutina")
    private List<Rutina_Ejercicio> rutina_ejercicios;

    private Float temperatura_promedio;

    @Column(name = "humedad_promedio")
    private Float humedad_promedio;

    private Integer presion_promedio;


}