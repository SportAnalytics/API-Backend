package com.api.sportanalytics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.api.sportanalytics.model.Rutina;
@Repository
public interface RutinaRepository extends JpaRepository<Rutina, Long> {
    
}
