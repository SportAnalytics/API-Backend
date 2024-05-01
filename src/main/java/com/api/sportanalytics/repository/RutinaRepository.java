package com.api.sportanalytics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.api.sportanalytics.model.Rutina;

import java.util.List;

@Repository
public interface RutinaRepository extends JpaRepository<Rutina, Long> {
    @Modifying
    @Query("UPDATE Rutina r SET r.estado = :estado WHERE r.atleta.id = :atletaId")
    void updateAllByAtletaId(@Param("atletaId") Long atletaId, @Param("estado") String estado);

    Rutina findFirstByAtletaIdAndEstadoOrderByFechaDesc(@Param("atletaId") Long atletaId, @Param("estado") String estado);

    List<Rutina> findRutinaByAtletaIdOrderByIdDesc(Long atletaId);

}
