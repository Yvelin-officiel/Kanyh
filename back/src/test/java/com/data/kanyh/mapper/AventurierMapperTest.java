package com.data.kanyh.mapper;

import com.data.kanyh.dto.AventurierDTO;
import com.data.kanyh.dto.AventurierInputDTO;
import com.data.kanyh.dto.AventurierUpdateDTO;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.model.Specialite;
import com.data.kanyh.repository.SpecialiteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AventurierMapperTest {

    @Mock
    private SpecialiteRepository specialiteRepository;

    @InjectMocks
    private AventurierMapper aventurierMapper;

    @Test
    void testToDTO_ShouldMapAllFieldsCorrectly() {
        Aventurier aventurier = new Aventurier();
        aventurier.setId(1L);
        aventurier.setNom("Jean Dupont");
        aventurier.setSpecialite(new Specialite(1, "Archer"));
        aventurier.setNiveauExperience(5);
        aventurier.setTauxJournalierBase(500.0);
        aventurier.setDisponibilite("Aujourd'hui");
        aventurier.setDateDisponibilite(LocalDate.of(2024, 1, 1));

        AventurierDTO result = aventurierMapper.toDTO(aventurier);

        assertEquals(1L, result.getId());
        assertEquals("Jean Dupont", result.getNom());
        assertEquals(aventurier.getSpecialite(), result.getSpecialite());
        assertEquals(1, result.getSpecialite().getId());
        assertEquals("Archer", result.getSpecialite().getNom());
        assertEquals(5, result.getNiveauExperience());
        assertEquals(500.0, result.getTauxJournalierBase());
        assertEquals("Aujourd'hui", result.getDisponibilite());
        assertEquals(LocalDate.of(2024, 1, 1), result.getDateDisponibilite());
    }

    @Test
    void testToEntity_ShouldMapAllFieldsCorrectly() {
        AventurierInputDTO dto = new AventurierInputDTO();
        dto.setNom("Marie Martin");
        dto.setSpecialiteId("1");
        dto.setTauxJournalierBase(600.0);

        Specialite specialite = new Specialite(1, "Python");
        when(specialiteRepository.findById(1)).thenReturn(Optional.of(specialite));

        Aventurier result = aventurierMapper.toEntity(dto);

        assertNull(result.getId());
        assertEquals("Marie Martin", result.getNom());
        assertEquals(specialite, result.getSpecialite());
        assertEquals(600.0, result.getTauxJournalierBase());
    }

    @Test
    void testUpdateEntityFromDTO_ShouldUpdateAllFieldsCorrectly() {
        Aventurier entity = new Aventurier();
        entity.setId(1L);
        entity.setNom("Ancien Nom");
        entity.setSpecialite(new Specialite(2, "Guerrier"));

        AventurierUpdateDTO dto = new AventurierUpdateDTO();
        dto.setNom("Nouveau Nom");
        dto.setSpecialiteId("2");
        dto.setNiveauExperience(7);
        dto.setTauxJournalierBase(700.0);
        dto.setDisponibilite("Aujourd'hui");
        dto.setDateDisponibilite(LocalDate.of(2024, 6, 1));

        Specialite specialite = new Specialite(2, "Guerrier");
        when(specialiteRepository.findById(2)).thenReturn(Optional.of(specialite));

        aventurierMapper.updateEntityFromDTO(dto, entity);

        assertEquals(1L, entity.getId());
        assertEquals("Nouveau Nom", entity.getNom());
        assertEquals(specialite, entity.getSpecialite());
        assertEquals(2, entity.getSpecialite().getId());
        assertEquals("Guerrier", entity.getSpecialite().getNom());
        assertEquals(7, entity.getNiveauExperience());
        assertEquals(700.0, entity.getTauxJournalierBase());
        assertEquals("Aujourd'hui", entity.getDisponibilite());
        assertEquals(LocalDate.of(2024, 6, 1), entity.getDateDisponibilite());
    }
}