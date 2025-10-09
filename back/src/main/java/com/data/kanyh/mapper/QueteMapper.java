package com.data.kanyh.mapper;


import com.data.kanyh.dto.QueteDTO;
import com.data.kanyh.dto.QueteInputDTO;
import com.data.kanyh.model.Quete;
import org.springframework.stereotype.Component;

@Component
public class QueteMapper {

    public QueteDTO toDTO(Quete quete){
        QueteDTO dto = new QueteDTO();
        dto.setId(quete.getId());
        dto.setNom(quete.getNom());
        dto.setDescription(quete.getDescription());
        dto.setPrime(quete.getPrime());
        dto.setDureeEstimee(quete.getDureeEstimee());
        dto.setDatePeremption(quete.getDatePeremption());
        dto.setExperienceGagnee(quete.getExperienceGagnee());
        dto.setStatut(quete.getStatut().name());
        dto.setCommanditaireId(quete.getCommanditaireId());
        dto.setEquipeId(quete.getEquipeId());
        return dto;
    }

    public Quete toEntity(QueteInputDTO input){
        Quete quete = new Quete();
        quete.setNom(input.getNom());
        quete.setDescription(input.getDescription());
        quete.setPrime(input.getPrime());
        quete.setDureeEstimee(input.getDureeEstimee());
        quete.setDatePeremption(input.getDatePeremption());
        quete.setStatut(com.data.kanyh.model.StatutQuete.NOUVELLE);
        return quete;
    }

    public void updateEntityFromDTO(QueteInputDTO dto, Quete entity) {
        if (dto.getNom() != null) {
            entity.setNom(dto.getNom());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getPrime() != null) {
            entity.setPrime(dto.getPrime());
        }
        if (dto.getDureeEstimee() != null) {
            entity.setDureeEstimee(dto.getDureeEstimee());
        }
        if (dto.getDatePeremption() != null) {
            entity.setDatePeremption(dto.getDatePeremption());
        }
    }
}
