package com.data.kanyh.repository;

import com.data.kanyh.dto.QueteDTO;
import com.data.kanyh.model.Quete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QueteRepository extends JpaRepository<Quete, Long> {

    @Query("SELECT q FROM Quete q " +
            "JOIN Equipe e ON q.equipeId = e.id " +
            "JOIN e.participations p " +
            "WHERE p.aventurier.id = :aventurierId " +
            "ORDER BY q.dateCreation DESC")
    List<Quete> findQuetesByAventurierId(@Param("aventurierId") Long aventurierId);

    List<Quete> findByCommanditaireId(Long commanditaireId);
}
