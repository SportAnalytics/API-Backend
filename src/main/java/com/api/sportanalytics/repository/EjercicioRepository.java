package com.api.sportanalytics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.api.sportanalytics.model.Ejercicio;
@Repository
public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {
    
}