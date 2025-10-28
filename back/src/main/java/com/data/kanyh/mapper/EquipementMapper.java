package com.data.kanyh.mapper;

import com.data.kanyh.dto.EquipementDTO;
import com.data.kanyh.dto.EquipementInputDTO;
import com.data.kanyh.dto.EquipementUpdateDTO;
import com.data.kanyh.model.Equipement;
import com.data.kanyh.repository.EquipementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EquipementMapper {

    @Autowired
    private EquipementRepository equipementRepository;

    /**
     * Convertit une entité Equipement en DTO.
     *
     * @param equipement l'entité Equipement à convertir
     * @return le DTO contenant les données de l'équipement
     */
    public EquipementDTO toDTO(Equipement equipement) {
        EquipementDTO dto = new EquipementDTO();
        dto.setId(equipement.getId());
        dto.setNom(equipement.getNom());
        dto.setType(equipement.getType());
        dto.setDurabiliteMax(equipement.getDurabiliteMax());
        dto.setDurabiliteRestante(equipement.getDurabiliteRestante());
        dto.setDisponibilite(equipement.getDisponibilite());
        dto.setDateRetourPrevue(equipement.getDateRetourPrevue());
        dto.setCoutReparation(equipement.getCoutReparation());
        return dto;
    }


    /**
     * Convertit un DTO d'entrée en entité Equipement.
     *
     * @param dto le DTO contenant les données pour créer un équipement
     * @return l'entité Equipement nouvellement créée
     */
    public Equipement toEntity(EquipementInputDTO dto) {
        Equipement equipement = new Equipement();
        equipement.setNom(dto.getNom());
        equipement.setType(dto.getType());
        equipement.setDurabiliteMax(dto.getDurabiliteMax());
        equipement.setDurabiliteRestante(dto.getDurabiliteRestante());
        equipement.setDisponibilite(dto.getDisponibilite());
        equipement.setDateRetourPrevue(dto.getDateRetourPrevue());
        equipement.setCoutReparation(dto.getCoutReparation());
        return equipement;
    }

    public void updateEntityFromDTO(EquipementUpdateDTO dto, Equipement equipement) {
        equipement.setNom(dto.getNom());
        equipement.setType(dto.getType());
        equipement.setDurabiliteMax(dto.getDurabiliteMax());
        equipement.setDurabiliteRestante(dto.getDurabiliteRestante());
        equipement.setDisponibilite(dto.getDisponibilite());
        equipement.setDateRetourPrevue(dto.getDateRetourPrevue());
        equipement.setCoutReparation(dto.getCoutReparation());
    }
}
