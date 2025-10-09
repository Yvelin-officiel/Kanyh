package com.data.kanyh.mapper;

import com.data.kanyh.dto.AventurierDTO;
import com.data.kanyh.dto.AventurierInputDTO;
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

    public Aventurier toEntity(AventurierDTO dto) {
        Aventurier aventurier = new Aventurier();
        aventurier.setNom(dto.getNom());
        aventurier.setSpecialite(dto.getSpecialite());
        aventurier.setNiveau(dto.getNiveau());
        aventurier.setTauxJournalierBase(dto.getTauxJournalierBase());
        aventurier.setDisponibilite(dto.getDisponibilite());
        aventurier.setDateDebut(dto.getDateDebut());
        return aventurier;
    }

    public Aventurier toEntity(AventurierInputDTO dto) {
        Aventurier aventurier = new Aventurier();
        aventurier.setNom(dto.getNom());
        aventurier.setSpecialite(dto.getSpecialite());
        aventurier.setTauxJournalierBase(dto.getTauxJournalierBase().doubleValue());
        return aventurier;
    }
}
