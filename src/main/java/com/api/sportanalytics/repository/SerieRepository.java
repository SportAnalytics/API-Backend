package com.api.sportanalytics.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.api.sportanalytics.model.Serie;

@Repository
public interface SerieRepository  extends JpaRepository<Serie, Long> {
    
}
