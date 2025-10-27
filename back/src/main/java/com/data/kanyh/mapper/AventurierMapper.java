package com.data.kanyh.mapper;

import com.data.kanyh.dto.AventurierDTO;
import com.data.kanyh.dto.AventurierInputDTO;
import com.data.kanyh.dto.AventurierUpdateDTO;
import com.data.kanyh.model.Aventurier;
import org.springframework.stereotype.Component;

@Component
public class AventurierMapper {

    /**
     * Convertit une entité Aventurier en DTO.
     *
     * @param aventurier l'entité Aventurier à convertir
     * @return le DTO contenant les données de l'aventurier
     */
    public AventurierDTO toDTO(Aventurier aventurier) {
        AventurierDTO dto = new AventurierDTO();
        dto.setId(aventurier.getId());
        dto.setNom(aventurier.getNom());
        dto.setSpecialite(aventurier.getSpecialite());
        dto.setNiveauExperience(aventurier.getNiveauExperience());
        dto.setTauxJournalierBase(aventurier.getTauxJournalierBase());
        dto.setDisponibilite(aventurier.getDisponibilite());
        dto.setDateDisponibilite(aventurier.getDateDisponibilite());
        return dto;
    }

    /**
     * Convertit un DTO d'entrée en entité Aventurier.
     *
     * @param dto le DTO contenant les données pour créer un aventurier
     * @return l'entité Aventurier nouvellement créée
     */
    public Aventurier toEntity(AventurierInputDTO dto) {
        Aventurier aventurier = new Aventurier();
        aventurier.setNom(dto.getNom());
        aventurier.setSpecialite(dto.getSpecialite());
        aventurier.setTauxJournalierBase(dto.getTauxJournalierBase());
        return aventurier;
    }

    /**
     * Met à jour une entité Aventurier existante avec les données d'un DTO de mise à jour.
     *
     * @param dto        le DTO contenant les nouvelles données
     * @param aventurier l'entité Aventurier à mettre à jour
     */
    public void updateEntityFromDTO(AventurierUpdateDTO dto, Aventurier aventurier) {
        aventurier.setNom(dto.getNom());
        aventurier.setSpecialite(dto.getSpecialite());
        aventurier.setNiveauExperience(dto.getNiveauExperience());
        aventurier.setTauxJournalierBase(dto.getTauxJournalierBase());
        aventurier.setDisponibilite(dto.getDisponibilite());
        aventurier.setDateDisponibilite(dto.getDateDisponibilite());
    }
}
