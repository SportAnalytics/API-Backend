package com.api.sportanalytics.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.api.sportanalytics.model.Rutina_Ejercicio;
@Repository
public interface RutinaEjercicioRepository extends JpaRepository<Rutina_Ejercicio, Long> {
    @Query("SELECT re FROM Rutina_Ejercicio re "
            + "JOIN re.ejercicio e "
            + "WHERE re.rutina.atleta.id = :atletaId AND e.nombre = :nombre")
    List<Rutina_Ejercicio> findByRutinaAtletaIdAndEjercicioNombre(@Param("atletaId") Long atletaId, @Param("nombre") String nombre);

    @Query("SELECT DISTINCT e.nombre FROM Rutina_Ejercicio re "
            + "JOIN re.ejercicio e "
            + "JOIN re.rutina r "
            + "WHERE r.atleta.id = :atletaId "
            + "AND e.tipo = 'sprints' ")
    List<String> findDistinctExerciseNamesByAtletaId(@Param("atletaId") Long atletaId);


}
