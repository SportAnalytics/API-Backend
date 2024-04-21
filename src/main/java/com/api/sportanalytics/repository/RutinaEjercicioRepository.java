package com.api.sportanalytics.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.api.sportanalytics.model.Rutina_Ejercicio;
@Repository
public interface RutinaEjercicioRepository extends JpaRepository<Rutina_Ejercicio, Long> {
    
}