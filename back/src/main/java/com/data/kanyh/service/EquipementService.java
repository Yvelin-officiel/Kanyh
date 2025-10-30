package com.data.kanyh.service;

import com.data.kanyh.dto.EquipementDTO;
import com.data.kanyh.dto.EquipementInputDTO;
import com.data.kanyh.dto.EquipementUpdateDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.EquipementMapper;
import com.data.kanyh.model.Equipement;
import com.data.kanyh.repository.EquipementRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EquipementService {

    private final EquipementRepository equipementRepository;
    private final EquipementMapper equipementMapper;

    private static final String NOT_FOUND = "Equipement non trouvé";


    public EquipementService(EquipementRepository equipementRepository, EquipementMapper equipementMapper) {
        this.equipementRepository = equipementRepository;
        this.equipementMapper = equipementMapper;
    }

    public EquipementDTO save (EquipementInputDTO equipement) {
        Equipement newEquipement = equipementRepository.save(equipementMapper.toEntity(equipement));
        return equipementMapper.toDTO(newEquipement);
    }

    public List<EquipementDTO> getAllEquipement () {
        List<Equipement> equipements = equipementRepository.findAll();
        return equipements.stream()
                .map(equipementMapper::toDTO)
                .toList();
    }

    public void delete (Long id) {
        if (!equipementRepository.existsById(id)) {
            throw new NotFoundException(NOT_FOUND);
        } else {
            equipementRepository.deleteById(id);
        }
    }

    public EquipementDTO update (EquipementUpdateDTO dto) {
        Equipement equipement = equipementRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        equipementMapper.updateEntityFromDTO(dto, equipement);
        Equipement updatedEquipement = equipementRepository.save(equipement);
        return equipementMapper.toDTO(updatedEquipement);
    }
}
