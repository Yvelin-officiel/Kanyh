package com.data.kanyh.service;

import com.data.kanyh.dto.AventurierDTO;
import com.data.kanyh.dto.ParticipationEquipeDTO;
import com.data.kanyh.dto.ParticipationEquipeInputDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.AventurierMapper;
import com.data.kanyh.mapper.ParticipationEquipeMapper;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.model.Equipe;
import com.data.kanyh.model.ParticipationEquipe;
import com.data.kanyh.repository.AventurierRepository;
import com.data.kanyh.repository.EquipeRepository;
import com.data.kanyh.repository.ParticipationEquipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ParticipationEquipeService {

    private final ParticipationEquipeRepository participationEquipeRepository;
    private final EquipeRepository equipeRepository;
    private final AventurierRepository aventurierRepository;
    private final ParticipationEquipeMapper participationEquipeMapper;
    private final AventurierMapper aventurierMapper;

    /**
     * Crée une nouvelle participation d'un aventurier à une équipe.
     *
     * @param input les données de la participation
     * @return le DTO de la participation créée
     * @throws NotFoundException si l'équipe ou l'aventurier n'existe pas
     */
    public ParticipationEquipeDTO createParticipation(ParticipationEquipeInputDTO input) {
        Equipe equipe = equipeRepository.findById(input.getEquipeId())
                .orElseThrow(() -> new NotFoundException("Équipe non trouvée"));

        Aventurier aventurier = aventurierRepository.findById(input.getAventurierId())
                .orElseThrow(() -> new NotFoundException("Aventurier non trouvé"));

        ParticipationEquipe participation = participationEquipeMapper.toEntity(input, equipe, aventurier);
        ParticipationEquipe saved = participationEquipeRepository.save(participation);

        return participationEquipeMapper.toDTO(saved);
    }

    /**
     * Récupère toutes les participations d'une équipe.
     *
     * @param equipeId l'identifiant de l'équipe
     * @return la liste des participations
     */
    public List<ParticipationEquipeDTO> getParticipationsByEquipe(Long equipeId) {
        return participationEquipeRepository.findByEquipeId(equipeId)
                .stream()
                .map(participationEquipeMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère toutes les participations d'un aventurier.
     *
     * @param aventurierId l'identifiant de l'aventurier
     * @return la liste des participations
     */
    public List<ParticipationEquipeDTO> getParticipationsByAventurier(Long aventurierId) {
        return participationEquipeRepository.findByAventurierId(aventurierId)
                .stream()
                .map(participationEquipeMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère tous les aventuriers disponibles sur une période donnée.
     * Un aventurier est disponible s'il n'a pas de participation qui chevauche la période demandée.
     *
     * @param dateDebut date de début de la période
     * @param dateFin date de fin de la période
     * @return la liste des aventuriers disponibles
     */
    public List<AventurierDTO> getAventuriersDisponibles(LocalDate dateDebut, LocalDate dateFin) {
        // Récupère tous les aventuriers
        List<Aventurier> allAventuriers = aventurierRepository.findAll();

        // Récupère les IDs des aventuriers occupés pendant la période
        List<Long> aventuriersOccupesIds = allAventuriers.stream()
                .map(Aventurier::getId)
                .filter(aventurierId -> {
                    List<ParticipationEquipe> participations =
                            participationEquipeRepository.findParticipationsByAventurierAndDateRange(
                                    aventurierId, dateDebut, dateFin);
                    return !participations.isEmpty();
                })
                .collect(Collectors.toList());

        // Filtre les aventuriers disponibles
        return allAventuriers.stream()
                .filter(aventurier -> !aventuriersOccupesIds.contains(aventurier.getId()))
                .map(aventurierMapper::toDTO)
                .collect(Collectors.toList());
    }
}
