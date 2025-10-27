package com.data.kanyh.mapper;

import com.data.kanyh.dto.QueteDTO;
import com.data.kanyh.dto.QueteInputDTO;
import com.data.kanyh.model.Quete;
import com.data.kanyh.model.StatutQuete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("QueteMapper - Tests unitaires")
class QueteMapperTest {

    private QueteMapper queteMapper;

    @BeforeEach
    void setUp() {
        queteMapper = new QueteMapper();
    }

    @Test
    @DisplayName("toDTO - Doit convertir une entité Quete en QueteDTO avec tous les champs")
    void toDTO_ShouldConvertEntityToDTO_WithAllFields() {
        // Given
        Quete quete = new Quete();
        quete.setId(1L);
        quete.setNom("Sauver la princesse");
        quete.setDescription("Une quête héroïque");
        quete.setPrime(1000.0);
        quete.setDureeEstimee(5);
        quete.setDatePeremption(LocalDate.of(2025, 12, 31));
        quete.setExperienceGagnee(500);
        quete.setStatut(StatutQuete.EN_COURS);
        quete.setCommanditaireId(10L);
        quete.setEquipeId(20L);

        // When
        QueteDTO dto = queteMapper.toDTO(quete);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNom()).isEqualTo("Sauver la princesse");
        assertThat(dto.getDescription()).isEqualTo("Une quête héroïque");
        assertThat(dto.getPrime()).isEqualTo(1000.0);
        assertThat(dto.getDureeEstimee()).isEqualTo(5);
        assertThat(dto.getDatePeremption()).isEqualTo(LocalDate.of(2025, 12, 31));
        assertThat(dto.getExperienceGagnee()).isEqualTo(500);
        assertThat(dto.getStatut()).isEqualTo("EN_COURS");
        assertThat(dto.getCommanditaireId()).isEqualTo(10L);
        assertThat(dto.getEquipeId()).isEqualTo(20L);
    }

    @Test
    @DisplayName("toDTO - Doit convertir le statut enum en String correctement")
    void toDTO_ShouldConvertStatutEnumToString() {
        // Given
        Quete quete = new Quete();
        quete.setId(1L);
        quete.setNom("Test");
        quete.setDescription("Test");
        quete.setPrime(100.0);
        quete.setDureeEstimee(1);
        quete.setDatePeremption(LocalDate.now());
        quete.setStatut(StatutQuete.NOUVELLE);

        // When
        QueteDTO dto = queteMapper.toDTO(quete);

        // Then
        assertThat(dto.getStatut()).isEqualTo("NOUVELLE");
    }

    @Test
    @DisplayName("toDTO - Doit gérer les champs optionnels null")
    void toDTO_ShouldHandleNullOptionalFields() {
        // Given
        Quete quete = new Quete();
        quete.setId(1L);
        quete.setNom("Quête minimale");
        quete.setDescription("Description");
        quete.setPrime(100.0);
        quete.setDureeEstimee(1);
        quete.setDatePeremption(LocalDate.now());
        quete.setStatut(StatutQuete.NOUVELLE);

        // When
        QueteDTO dto = queteMapper.toDTO(quete);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getExperienceGagnee()).isNull();
        assertThat(dto.getCommanditaireId()).isNull();
        assertThat(dto.getEquipeId()).isNull();
    }

    @Test
    @DisplayName("toEntity - Doit convertir un QueteInputDTO en entité Quete")
    void toEntity_ShouldConvertInputDTOToEntity() {
        // Given
        QueteInputDTO inputDTO = new QueteInputDTO();
        inputDTO.setNom("Nouvelle quête");
        inputDTO.setDescription("Une description passionnante");
        inputDTO.setPrime(2500.0);
        inputDTO.setDureeEstimee(10);
        inputDTO.setDatePeremption(LocalDate.of(2026, 6, 15));
        inputDTO.setStatut("EN_COURS");

        // When
        Quete quete = queteMapper.toEntity(inputDTO);

        // Then
        assertThat(quete).isNotNull();
        assertThat(quete.getId()).isNull();
        assertThat(quete.getNom()).isEqualTo("Nouvelle quête");
        assertThat(quete.getDescription()).isEqualTo("Une description passionnante");
        assertThat(quete.getPrime()).isEqualTo(2500.0);
        assertThat(quete.getDureeEstimee()).isEqualTo(10);
        assertThat(quete.getDatePeremption()).isEqualTo(LocalDate.of(2026, 6, 15));
        assertThat(quete.getStatut()).isEqualTo(StatutQuete.NOUVELLE);
        assertThat(quete.getExperienceGagnee()).isNull();
        assertThat(quete.getCommanditaireId()).isNull();
        assertThat(quete.getEquipeId()).isNull();
    }

    @Test
    @DisplayName("toEntity - Doit toujours définir le statut à NOUVELLE")
    void toEntity_ShouldAlwaysSetStatusToNouvelle() {
        QueteInputDTO inputDTO = new QueteInputDTO();
        inputDTO.setNom("Test");
        inputDTO.setDescription("Test");
        inputDTO.setPrime(100.0);
        inputDTO.setDureeEstimee(1);
        inputDTO.setDatePeremption(LocalDate.now());
        inputDTO.setStatut("TERMINEE");

        Quete quete = queteMapper.toEntity(inputDTO);

        assertThat(quete.getStatut()).isEqualTo(StatutQuete.NOUVELLE);
    }

    @Test
    @DisplayName("updateEntityFromDTO - Doit mettre à jour tous les champs non-null")
    void updateEntityFromDTO_ShouldUpdateAllNonNullFields() {
        Quete existingQuete = new Quete();
        existingQuete.setId(1L);
        existingQuete.setNom("Ancien nom");
        existingQuete.setDescription("Ancienne description");
        existingQuete.setPrime(500.0);
        existingQuete.setDureeEstimee(3);
        existingQuete.setDatePeremption(LocalDate.of(2025, 1, 1));
        existingQuete.setStatut(StatutQuete.EN_COURS);

        QueteInputDTO updateDTO = new QueteInputDTO();
        updateDTO.setNom("Nouveau nom");
        updateDTO.setDescription("Nouvelle description");
        updateDTO.setPrime(1500.0);
        updateDTO.setDureeEstimee(7);
        updateDTO.setDatePeremption(LocalDate.of(2025, 12, 31));

        queteMapper.updateEntityFromDTO(updateDTO, existingQuete);

        assertThat(existingQuete.getId()).isEqualTo(1L);
        assertThat(existingQuete.getNom()).isEqualTo("Nouveau nom");
        assertThat(existingQuete.getDescription()).isEqualTo("Nouvelle description");
        assertThat(existingQuete.getPrime()).isEqualTo(1500.0);
        assertThat(existingQuete.getDureeEstimee()).isEqualTo(7);
        assertThat(existingQuete.getDatePeremption()).isEqualTo(LocalDate.of(2025, 12, 31));
        assertThat(existingQuete.getStatut()).isEqualTo(StatutQuete.EN_COURS);
    }

    @Test
    @DisplayName("updateEntityFromDTO - Ne doit pas mettre à jour les champs null (mise à jour partielle)")
    void updateEntityFromDTO_ShouldNotUpdateNullFields_PartialUpdate() {
        Quete existingQuete = new Quete();
        existingQuete.setId(1L);
        existingQuete.setNom("Ancien nom");
        existingQuete.setDescription("Ancienne description");
        existingQuete.setPrime(500.0);
        existingQuete.setDureeEstimee(3);
        existingQuete.setDatePeremption(LocalDate.of(2025, 1, 1));

        QueteInputDTO partialUpdateDTO = new QueteInputDTO();
        partialUpdateDTO.setNom("Nom mis à jour");
        queteMapper.updateEntityFromDTO(partialUpdateDTO, existingQuete);

        assertThat(existingQuete.getNom()).isEqualTo("Nom mis à jour"); // Mis à jour
        assertThat(existingQuete.getDescription()).isEqualTo("Ancienne description");
        assertThat(existingQuete.getPrime()).isEqualTo(500.0);
        assertThat(existingQuete.getDureeEstimee()).isEqualTo(3);
        assertThat(existingQuete.getDatePeremption()).isEqualTo(LocalDate.of(2025, 1, 1));
    }

    @Test
    @DisplayName("updateEntityFromDTO - Ne doit rien modifier si tous les champs sont null")
    void updateEntityFromDTO_ShouldNotModifyEntity_WhenAllFieldsAreNull() {
        Quete existingQuete = new Quete();
        existingQuete.setId(1L);
        existingQuete.setNom("Original");
        existingQuete.setDescription("Original description");
        existingQuete.setPrime(1000.0);
        existingQuete.setDureeEstimee(5);
        existingQuete.setDatePeremption(LocalDate.of(2025, 6, 1));

        QueteInputDTO emptyUpdateDTO = new QueteInputDTO();

        queteMapper.updateEntityFromDTO(emptyUpdateDTO, existingQuete);

        assertThat(existingQuete.getNom()).isEqualTo("Original");
        assertThat(existingQuete.getDescription()).isEqualTo("Original description");
        assertThat(existingQuete.getPrime()).isEqualTo(1000.0);
        assertThat(existingQuete.getDureeEstimee()).isEqualTo(5);
        assertThat(existingQuete.getDatePeremption()).isEqualTo(LocalDate.of(2025, 6, 1));
    }

    @Test
    @DisplayName("updateEntityFromDTO - Doit permettre la mise à jour sélective de plusieurs champs")
    void updateEntityFromDTO_ShouldUpdateMultipleSelectedFields() {
        Quete existingQuete = new Quete();
        existingQuete.setId(1L);
        existingQuete.setNom("Quête 1");
        existingQuete.setDescription("Description 1");
        existingQuete.setPrime(100.0);
        existingQuete.setDureeEstimee(2);
        existingQuete.setDatePeremption(LocalDate.of(2025, 3, 1));

        QueteInputDTO updateDTO = new QueteInputDTO();
        updateDTO.setPrime(500.0);
        updateDTO.setDureeEstimee(8);

        queteMapper.updateEntityFromDTO(updateDTO, existingQuete);

        assertThat(existingQuete.getNom()).isEqualTo("Quête 1");
        assertThat(existingQuete.getDescription()).isEqualTo("Description 1");
        assertThat(existingQuete.getPrime()).isEqualTo(500.0);
        assertThat(existingQuete.getDureeEstimee()).isEqualTo(8);
        assertThat(existingQuete.getDatePeremption()).isEqualTo(LocalDate.of(2025, 3, 1));
    }
}
