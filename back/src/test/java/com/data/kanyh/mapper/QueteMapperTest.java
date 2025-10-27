package com.data.kanyh.mapper;

import com.data.kanyh.dto.QueteDTO;
import com.data.kanyh.dto.QueteInputDTO;
import com.data.kanyh.model.Quete;
import com.data.kanyh.model.Specialite;
import com.data.kanyh.model.StatutQuete;
import com.data.kanyh.repository.SpecialiteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("QueteMapper - Tests unitaires")
class QueteMapperTest {

    @Mock
    private SpecialiteRepository specialiteRepository;

    @InjectMocks
    private QueteMapper queteMapper;

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
        inputDTO.setSpecialitesRequisesIds(null);

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
        assertThat(quete.getSpecialitesRequises()).isEmpty();
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

    @Test
    @DisplayName("toEntity - Doit convertir les IDs de spécialités en objets Specialite")
    void toEntity_ShouldConvertSpecialiteIdsToSpecialiteObjects() {
        QueteInputDTO inputDTO = new QueteInputDTO();
        inputDTO.setNom("Quête avec spécialités");
        inputDTO.setDescription("Description");
        inputDTO.setPrime(1000.0);
        inputDTO.setDureeEstimee(5);
        inputDTO.setDatePeremption(LocalDate.of(2025, 12, 31));
        inputDTO.setStatut("NOUVELLE");
        inputDTO.setSpecialitesRequisesIds(Arrays.asList("1", "2"));

        Specialite specialite1 = new Specialite(1, "Archer");
        Specialite specialite2 = new Specialite(2, "Guerrier");

        when(specialiteRepository.findById(1)).thenReturn(Optional.of(specialite1));
        when(specialiteRepository.findById(2)).thenReturn(Optional.of(specialite2));

        Quete quete = queteMapper.toEntity(inputDTO);

        assertThat(quete.getSpecialitesRequises()).isNotNull();
        assertThat(quete.getSpecialitesRequises()).hasSize(2);
        assertThat(quete.getSpecialitesRequises()).containsExactly(specialite1, specialite2);
    }

    @Test
    @DisplayName("toEntity - Doit créer une liste vide quand specialitesRequisesIds est vide")
    void toEntity_ShouldCreateEmptyList_WhenSpecialitesRequisesIdsIsEmpty() {
        QueteInputDTO inputDTO = new QueteInputDTO();
        inputDTO.setNom("Quête sans spécialités");
        inputDTO.setDescription("Description");
        inputDTO.setPrime(1000.0);
        inputDTO.setDureeEstimee(5);
        inputDTO.setDatePeremption(LocalDate.of(2025, 12, 31));
        inputDTO.setStatut("NOUVELLE");
        inputDTO.setSpecialitesRequisesIds(Arrays.asList());

        Quete quete = queteMapper.toEntity(inputDTO);

        assertThat(quete.getSpecialitesRequises()).isNotNull();
        assertThat(quete.getSpecialitesRequises()).isEmpty();
    }

    @Test
    @DisplayName("toEntity - Doit lancer une exception quand une spécialité n'existe pas")
    void toEntity_ShouldThrowException_WhenSpecialiteNotFound() {
        QueteInputDTO inputDTO = new QueteInputDTO();
        inputDTO.setNom("Quête avec spécialité invalide");
        inputDTO.setDescription("Description");
        inputDTO.setPrime(1000.0);
        inputDTO.setDureeEstimee(5);
        inputDTO.setDatePeremption(LocalDate.of(2025, 12, 31));
        inputDTO.setStatut("NOUVELLE");
        inputDTO.setSpecialitesRequisesIds(Arrays.asList("999"));

        when(specialiteRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> queteMapper.toEntity(inputDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Specialite non trouvée avec l'id: 999");
    }

    @Test
    @DisplayName("updateEntityFromDTO - Doit mettre à jour les spécialités requises")
    void updateEntityFromDTO_ShouldUpdateSpecialitesRequises() {
        Specialite oldSpecialite = new Specialite(1, "Archer");
        Quete existingQuete = new Quete();
        existingQuete.setId(1L);
        existingQuete.setNom("Quête existante");
        existingQuete.setDescription("Description");
        existingQuete.setPrime(500.0);
        existingQuete.setDureeEstimee(3);
        existingQuete.setDatePeremption(LocalDate.of(2025, 1, 1));
        existingQuete.setSpecialitesRequises(Arrays.asList(oldSpecialite));

        QueteInputDTO updateDTO = new QueteInputDTO();
        updateDTO.setSpecialitesRequisesIds(Arrays.asList("2", "3"));

        Specialite specialite2 = new Specialite(2, "Guerrier");
        Specialite specialite3 = new Specialite(3, "Dermatologie");

        when(specialiteRepository.findById(2)).thenReturn(Optional.of(specialite2));
        when(specialiteRepository.findById(3)).thenReturn(Optional.of(specialite3));

        queteMapper.updateEntityFromDTO(updateDTO, existingQuete);

        assertThat(existingQuete.getSpecialitesRequises()).isNotNull();
        assertThat(existingQuete.getSpecialitesRequises()).hasSize(2);
        assertThat(existingQuete.getSpecialitesRequises()).containsExactly(specialite2, specialite3);
    }

    @Test
    @DisplayName("updateEntityFromDTO - Ne doit pas modifier les spécialités si specialitesRequisesIds est null")
    void updateEntityFromDTO_ShouldNotUpdateSpecialites_WhenSpecialitesRequisesIdsIsNull() {
        Specialite specialite = new Specialite(1, "Archer");
        List<Specialite> originalSpecialites = Arrays.asList(specialite);

        Quete existingQuete = new Quete();
        existingQuete.setId(1L);
        existingQuete.setNom("Quête existante");
        existingQuete.setDescription("Description");
        existingQuete.setPrime(500.0);
        existingQuete.setDureeEstimee(3);
        existingQuete.setDatePeremption(LocalDate.of(2025, 1, 1));
        existingQuete.setSpecialitesRequises(originalSpecialites);

        QueteInputDTO updateDTO = new QueteInputDTO();
        updateDTO.setNom("Nouveau nom");
        updateDTO.setSpecialitesRequisesIds(null);

        queteMapper.updateEntityFromDTO(updateDTO, existingQuete);

        assertThat(existingQuete.getNom()).isEqualTo("Nouveau nom");
        assertThat(existingQuete.getSpecialitesRequises()).isEqualTo(originalSpecialites);
    }
}
