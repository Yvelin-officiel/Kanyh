package com.data.kanyh.mapper;

import com.data.kanyh.dto.AventurierDTO;
import com.data.kanyh.dto.AventurierInputDTO;
import com.data.kanyh.dto.AventurierUpdateDTO;
import com.data.kanyh.model.Aventurier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class AventurierMapperTest {

    @InjectMocks
    private AventurierMapper aventurierMapper;

    @Test
    void testToDTO_ShouldMapAllFieldsCorrectly() {
        Aventurier aventurier = new Aventurier();
        aventurier.setId(1L);
        aventurier.setNom("Jean Dupont");
        aventurier.setSpecialite("Java");
        aventurier.setNiveau(5);
        aventurier.setTauxJournalierBase(500.0);
        aventurier.setDisponibilite("Aujourd'hui");
        aventurier.setDateDebut(LocalDate.of(2024, 1, 1));

        AventurierDTO result = aventurierMapper.toDTO(aventurier);

        assertEquals(1L, result.getId());
        assertEquals("Jean Dupont", result.getNom());
        assertEquals("Java", result.getSpecialite());
        assertEquals(5, result.getNiveau());
        assertEquals(500.0, result.getTauxJournalierBase());
        assertEquals("Aujourd'hui", result.getDisponibilite());
        assertEquals(LocalDate.of(2024, 1, 1), result.getDateDebut());
    }

    @Test
    void testToEntity_ShouldMapAllFieldsCorrectly() {
        AventurierInputDTO dto = new AventurierInputDTO();
        dto.setNom("Marie Martin");
        dto.setSpecialite("Python");
        dto.setTauxJournalierBase(600.0);

        Aventurier result = aventurierMapper.toEntity(dto);

        assertNull(result.getId());
        assertEquals("Marie Martin", result.getNom());
        assertEquals("Python", result.getSpecialite());
        assertEquals(600.0, result.getTauxJournalierBase());
    }

    @Test
    void testUpdateEntityFromDTO_ShouldUpdateAllFieldsCorrectly() {
        Aventurier entity = new Aventurier();
        entity.setId(1L);
        entity.setNom("Ancien Nom");
        entity.setSpecialite("Ancienne Spécialité");

        AventurierUpdateDTO dto = new AventurierUpdateDTO();
        dto.setNom("Nouveau Nom");
        dto.setSpecialite("Nouvelle Spécialité");
        dto.setNiveauExperience(7);
        dto.setTauxJournalierBase(700.0);
        dto.setDisponibilite("Aujourd'hui");
        dto.setDateDisponibilite(LocalDate.of(2024, 6, 1));

        aventurierMapper.updateEntityFromDTO(dto, entity);

        assertEquals(1L, entity.getId());
        assertEquals("Nouveau Nom", entity.getNom());
        assertEquals("Nouvelle Spécialité", entity.getSpecialite());
        assertEquals(7, entity.getNiveau());
        assertEquals(700.0, entity.getTauxJournalierBase());
        assertEquals("Aujourd'hui", entity.getDisponibilite());
        assertEquals(LocalDate.of(2024, 6, 1), entity.getDateDebut());
    }
}