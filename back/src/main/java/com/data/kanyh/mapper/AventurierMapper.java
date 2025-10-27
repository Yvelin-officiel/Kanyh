package com.data.kanyh.mapper;

import com.data.kanyh.dto.AventurierDTO;
import com.data.kanyh.dto.AventurierInputDTO;
import com.data.kanyh.dto.AventurierUpdateDTO;
import com.data.kanyh.model.Aventurier;
import org.springframework.stereotype.Component;

@Component
public class AventurierMapper {

    public AventurierDTO toDTO(Aventurier aventurier) {
        AventurierDTO dto = new AventurierDTO();
        dto.setId(aventurier.getId());
        dto.setNom(aventurier.getNom());
        dto.setSpecialite(aventurier.getSpecialite());
        dto.setNiveau(aventurier.getNiveau());
        dto.setTauxJournalierBase(aventurier.getTauxJournalierBase());
        dto.setDisponibilite(aventurier.getDisponibilite());
        dto.setDateDebut(aventurier.getDateDebut());
        return dto;
    }

    public Aventurier toEntity(AventurierInputDTO dto) {
        Aventurier aventurier = new Aventurier();
        aventurier.setNom(dto.getNom());
        aventurier.setSpecialite(dto.getSpecialite());
        aventurier.setTauxJournalierBase(dto.getTauxJournalierBase().doubleValue());
        return aventurier;
    }

    public void updateEntityFromDTO(AventurierUpdateDTO dto, Aventurier entity) {
        entity.setNom(dto.getNom());
        entity.setSpecialite(dto.getSpecialite());
        entity.setNiveau(dto.getNiveauExperience());
        entity.setTauxJournalierBase(dto.getTauxJournalierBase());
        entity.setDisponibilite(dto.getDisponibilite());
        entity.setDateDebut(dto.getDateDisponibilite());
    }
}
