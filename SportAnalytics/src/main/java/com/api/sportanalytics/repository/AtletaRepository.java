package com.api.sportanalytics.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.api.sportanalytics.model.Atleta;

@Repository
public interface AtletaRepository extends JpaRepository<Atleta, Long> {
    
}
