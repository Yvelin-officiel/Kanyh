package com.data.kanyh.controller;

import com.data.kanyh.dto.AventurierDTO;
import com.data.kanyh.dto.ParticipationEquipeDTO;
import com.data.kanyh.dto.ParticipationEquipeInputDTO;
import com.data.kanyh.service.ParticipationEquipeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/participations")
@AllArgsConstructor
public class ParticipationEquipeController {

    private final ParticipationEquipeService participationEquipeService;

    /**
     * Crée une nouvelle participation d'un aventurier à une équipe.
     *
     * @param input les données de la participation
     * @return la participation créée
     */
    @PostMapping("/create")
    public ResponseEntity<ParticipationEquipeDTO> createParticipation(
            @Valid @RequestBody ParticipationEquipeInputDTO input) {
        ParticipationEquipeDTO created = participationEquipeService.createParticipation(input);
        return ResponseEntity.ok(created);
    }

    /**
     * Récupère toutes les participations d'une équipe.
     *
     * @param equipeId l'identifiant de l'équipe
     * @return la liste des participations
     */
    @GetMapping("/equipe/{equipeId}")
    public ResponseEntity<List<ParticipationEquipeDTO>> getParticipationsByEquipe(
            @PathVariable Long equipeId) {
        List<ParticipationEquipeDTO> participations = participationEquipeService.getParticipationsByEquipe(equipeId);
        return ResponseEntity.ok(participations);
    }

    /**
     * Récupère toutes les participations d'un aventurier.
     *
     * @param aventurierId l'identifiant de l'aventurier
     * @return la liste des participations
     */
    @GetMapping("/aventurier/{aventurierId}")
    public ResponseEntity<List<ParticipationEquipeDTO>> getParticipationsByAventurier(
            @PathVariable Long aventurierId) {
        List<ParticipationEquipeDTO> participations = participationEquipeService.getParticipationsByAventurier(aventurierId);
        return ResponseEntity.ok(participations);
    }

    /**
     * Récupère tous les aventuriers disponibles sur une période donnée.
     *
     * @param dateDebut date de début de la période (format: yyyy-MM-dd)
     * @param dateFin date de fin de la période (format: yyyy-MM-dd)
     * @return la liste des aventuriers disponibles
     */
    @GetMapping("/aventuriers-disponibles")
    public ResponseEntity<List<AventurierDTO>> getAventuriersDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<AventurierDTO> disponibles = participationEquipeService.getAventuriersDisponibles(dateDebut, dateFin);
        return ResponseEntity.ok(disponibles);
    }
}
