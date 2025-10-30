package com.data.kanyh.repository;

import com.data.kanyh.model.ParticipationEquipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ParticipationEquipeRepository extends JpaRepository<ParticipationEquipe, Long> {

    List<ParticipationEquipe> findByEquipeId(Long equipeId);

    List<ParticipationEquipe> findByAventurierId(Long aventurierId);

    @Query("SELECT p FROM ParticipationEquipe p WHERE p.aventurier.id = :aventurierId " +
           "AND ((p.dateAffectation BETWEEN :dateDebut AND :dateFin) " +
           "OR (p.dateRetour BETWEEN :dateDebut AND :dateFin) " +
           "OR (p.dateAffectation <= :dateDebut AND p.dateRetour >= :dateFin))")
    List<ParticipationEquipe> findParticipationsByAventurierAndDateRange(
            @Param("aventurierId") Long aventurierId,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );
}
