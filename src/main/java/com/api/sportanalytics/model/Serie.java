package com.api.sportanalytics.model;
import jakarta.persistence.Column;
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
import java.math.BigDecimal;
@Data
@Setter
@Getter
@Entity
@Table(name = "Serie")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_serie")
    private Integer numeroSerie;

    @ManyToOne
    @JoinColumn(name = "rutina_ejercicio_id")
    private Rutina_Ejercicio rutinaEjercicio;

    private BigDecimal tiempo;

    private BigDecimal velocidad;

    private BigDecimal fre_cardiaca;
}