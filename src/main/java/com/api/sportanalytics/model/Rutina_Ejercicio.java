package com.api.sportanalytics.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
@Entity
@Table(name = "Rutina_Ejercicio")
public class Rutina_Ejercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer cantidad_series;
    private String intensidad;
    private String descanso;
    @ManyToOne
    @JoinColumn(name = "ejercicio_id")
    private Ejercicio ejercicio;
    @ManyToOne
    @JoinColumn(name = "rutina_id")
    @JsonIgnore
    private Rutina rutina;
    @OneToMany(mappedBy = "rutina_ejercicio")
    private List<Serie> series;

}
