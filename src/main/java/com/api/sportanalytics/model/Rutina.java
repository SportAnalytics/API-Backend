package com.api.sportanalytics.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
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
    private Atleta atleta;
    private LocalDate fecha;
    private String estado; 

    @OneToMany(mappedBy = "rutina")
    private List<Rutina_Ejercicio> rutina_ejercicios;
}
