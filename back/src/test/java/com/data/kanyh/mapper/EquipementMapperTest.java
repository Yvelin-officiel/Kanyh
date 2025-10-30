package com.data.kanyh.mapper;

import com.data.kanyh.dto.EquipementDTO;
import com.data.kanyh.dto.EquipementInputDTO;
import com.data.kanyh.dto.EquipementUpdateDTO;
import com.data.kanyh.model.Equipement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EquipementMapperTest {

    @InjectMocks
    private EquipementMapper equipementMapper;

    @Test
    void toDTO_ShouldMapAllFieldsCorrectly() {
        Equipement equipement = new Equipement();
        equipement.setId(1L);
        equipement.setNom("Arc du Tireur");
        equipement.setType("Arc long");
        equipement.setDurabiliteMax(120);
        equipement.setDurabiliteRestante(100);
        equipement.setDisponibilite("disponible");
        equipement.setDateRetourPrevue("2025-11-15T10:00:00");
        equipement.setCoutReparation(15.5f);

        EquipementDTO dto = equipementMapper.toDTO(equipement);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Arc du Tireur", dto.getNom());
        assertEquals("Arc long", dto.getType());
        assertEquals(120, dto.getDurabiliteMax());
        assertEquals(100, dto.getDurabiliteRestante());
        assertEquals("disponible", dto.getDisponibilite());
        assertEquals("2025-11-15T10:00:00", dto.getDateRetourPrevue());
        assertEquals(15.5f, dto.getCoutReparation());
    }

    @Test
    void toEntity_ShouldMapAllFieldsCorrectly() {
        EquipementInputDTO input = new EquipementInputDTO();
        input.setNom("Arc du Tireur");
        input.setType("Arc long");
        input.setDurabiliteMax(120);
        input.setDurabiliteRestante(100);
        input.setDisponibilite("disponible");
        input.setDateRetourPrevue("2025-11-15T10:00:00");
        input.setCoutReparation(15.5f);

        Equipement entity = equipementMapper.toEntity(input);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("Arc du Tireur", entity.getNom());
        assertEquals("Arc long", entity.getType());
        assertEquals(120, entity.getDurabiliteMax());
        assertEquals(100, entity.getDurabiliteRestante());
        assertEquals("disponible", entity.getDisponibilite());
        assertEquals("2025-11-15T10:00:00", entity.getDateRetourPrevue());
        assertEquals(15.5f, entity.getCoutReparation());
    }

    @Test
    void updateEntityFromDTO_ShouldUpdateAllFields() {
        Equipement existing = new Equipement();
        existing.setId(2L);
        existing.setNom("Ancien Arc");
        existing.setType("Arc court");
        existing.setDurabiliteMax(80);
        existing.setDurabiliteRestante(50);
        existing.setDisponibilite("en réparation");
        existing.setDateRetourPrevue(null);
        existing.setCoutReparation(5.0f);

        EquipementUpdateDTO dto = new EquipementUpdateDTO();
        dto.setId(2L);
        dto.setNom("Arc du Tireur");
        dto.setType("Arc long");
        dto.setDurabiliteMax(120);
        dto.setDurabiliteRestante(110);
        dto.setDisponibilite("disponible");
        dto.setDateRetourPrevue("2025-11-20T09:00:00");
        dto.setCoutReparation(12.0f);

        equipementMapper.updateEntityFromDTO(dto, existing);

        assertEquals(2L, existing.getId());
        assertEquals("Arc du Tireur", existing.getNom());
        assertEquals("Arc long", existing.getType());
        assertEquals(120, existing.getDurabiliteMax());
        assertEquals(110, existing.getDurabiliteRestante());
        assertEquals("disponible", existing.getDisponibilite());
        assertEquals("2025-11-20T09:00:00", existing.getDateRetourPrevue());
        assertEquals(12.0f, existing.getCoutReparation());
    }
}
