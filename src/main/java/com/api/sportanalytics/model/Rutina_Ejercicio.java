package com.api.sportanalytics.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    private Rutina rutina;
}
