package com.data.kanyh.mapper;

import com.data.kanyh.dto.ParticipationEquipeDTO;
import com.data.kanyh.dto.ParticipationEquipeInputDTO;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.model.Equipe;
import com.data.kanyh.model.ParticipationEquipe;
import org.springframework.stereotype.Component;

@Component
public class ParticipationEquipeMapper {

    /**
     * Convertit une entité {@link ParticipationEquipe} en {@link ParticipationEquipeDTO}.
     *
     * @param participation l'entité {@link ParticipationEquipe} à convertir
     * @return le {@link ParticipationEquipeDTO} correspondant
     */
    public ParticipationEquipeDTO toDTO(ParticipationEquipe participation) {
        ParticipationEquipeDTO dto = new ParticipationEquipeDTO();
        dto.setId(participation.getId());
        dto.setEquipeId(participation.getEquipe().getId());
        dto.setEquipeNom(participation.getEquipe().getNom());
        dto.setAventurierId(participation.getAventurier().getId());
        dto.setAventurierNom(participation.getAventurier().getNom());
        dto.setDateAffectation(participation.getDateAffectation());
        dto.setDateRetour(participation.getDateRetour());
        dto.setEtat(participation.getEtat());
        dto.setGainExperience(participation.getGainExperience());
        return dto;
    }

    /**
     * Convertit un {@link ParticipationEquipeInputDTO} en entité {@link ParticipationEquipe}.
     *
     * @param input le {@link ParticipationEquipeInputDTO} contenant les données
     * @param equipe l'entité {@link Equipe} associée
     * @param aventurier l'entité {@link Aventurier} associée
     * @return l'entité {@link ParticipationEquipe} correspondante
     */
    public ParticipationEquipe toEntity(ParticipationEquipeInputDTO input, Equipe equipe, Aventurier aventurier) {
        ParticipationEquipe participation = new ParticipationEquipe();
        participation.setEquipe(equipe);
        participation.setAventurier(aventurier);
        participation.setDateAffectation(input.getDateAffectation());
        participation.setDateRetour(input.getDateRetour());
        participation.setEtat(input.getEtat());
        participation.setGainExperience(input.getGainExperience());
        return participation;
    }
}
