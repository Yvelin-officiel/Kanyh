package com.data.kanyh.service;

import com.data.kanyh.dto.QueteDTO;
import com.data.kanyh.dto.QueteInputDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.QueteMapper;
import com.data.kanyh.model.Quete;
import com.data.kanyh.model.StatutQuete;
import com.data.kanyh.repository.QueteRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("QueteService - Tests unitaires")
class QueteServiceTest {

    @Mock
    private QueteRepository queteRepository;

    @Mock
    private QueteMapper queteMapper;

    @InjectMocks
    private QueteService queteService;

    private Quete quete;
    private QueteDTO queteDTO;
    private QueteInputDTO queteInputDTO;

    @BeforeEach
    void setUp() {
        quete = new Quete();
        quete.setId(1L);
        quete.setNom("Sauver la princesse");
        quete.setDescription("Une quête héroïque");
        quete.setPrime(1000.0);
        quete.setDureeEstimee(5);
        quete.setDatePeremption(LocalDate.of(2025, 12, 31));
        quete.setExperienceGagnee(500);
        quete.setStatut(StatutQuete.NOUVELLE);
        quete.setCommanditaireId(10L);
        quete.setEquipeId(20L);

        queteDTO = new QueteDTO();
        queteDTO.setId(1L);
        queteDTO.setNom("Sauver la princesse");
        queteDTO.setDescription("Une quête héroïque");
        queteDTO.setPrime(1000.0);
        queteDTO.setDureeEstimee(5);
        queteDTO.setDatePeremption(LocalDate.of(2025, 12, 31));
        queteDTO.setExperienceGagnee(500);
        queteDTO.setStatut("NOUVELLE");
        queteDTO.setCommanditaireId(10L);
        queteDTO.setEquipeId(20L);

        queteInputDTO = new QueteInputDTO();
        queteInputDTO.setNom("Nouvelle quête");
        queteInputDTO.setDescription("Description de la quête");
        queteInputDTO.setPrime(2000.0);
        queteInputDTO.setDureeEstimee(10);
        queteInputDTO.setDatePeremption(LocalDate.of(2026, 6, 15));
        queteInputDTO.setStatut("NOUVELLE");
    }

    // ========== Tests pour getAllQuetes() ==========

    @Test
    @DisplayName("getAllQuetes - Doit retourner toutes les quêtes")
    void getAllQuetes_ShouldReturnAllQuetes() {
        Quete quete2 = new Quete();
        quete2.setId(2L);
        quete2.setNom("Tuer le dragon");
        quete2.setStatut(StatutQuete.EN_COURS);

        QueteDTO queteDTO2 = new QueteDTO();
        queteDTO2.setId(2L);
        queteDTO2.setNom("Tuer le dragon");
        queteDTO2.setStatut("EN_COURS");

        List<Quete> quetes = Arrays.asList(quete, quete2);

        when(queteRepository.findAll()).thenReturn(quetes);
        when(queteMapper.toDTO(quete)).thenReturn(queteDTO);
        when(queteMapper.toDTO(quete2)).thenReturn(queteDTO2);

        List<QueteDTO> result = queteService.getAllQuetes();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getNom()).isEqualTo("Sauver la princesse");
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getNom()).isEqualTo("Tuer le dragon");

        verify(queteRepository, times(1)).findAll();
        verify(queteMapper, times(1)).toDTO(quete);
        verify(queteMapper, times(1)).toDTO(quete2);
    }

    @Test
    @DisplayName("getAllQuetes - Doit retourner une liste vide si aucune quête")
    void getAllQuetes_ShouldReturnEmptyList_WhenNoQuetes() {
        when(queteRepository.findAll()).thenReturn(List.of());

        List<QueteDTO> result = queteService.getAllQuetes();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(queteRepository, times(1)).findAll();
        verify(queteMapper, never()).toDTO(any());
    }

    // ========== Tests pour getQueteById() ==========

    @Test
    @DisplayName("getQueteById - Doit retourner la quête correspondante")
    void getQueteById_ShouldReturnQuete_WhenIdExists() {
        when(queteRepository.findById(1L)).thenReturn(Optional.of(quete));
        when(queteMapper.toDTO(quete)).thenReturn(queteDTO);

        QueteDTO result = queteService.getQueteById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNom()).isEqualTo("Sauver la princesse");
        assertThat(result.getDescription()).isEqualTo("Une quête héroïque");

        verify(queteRepository, times(1)).findById(1L);
        verify(queteMapper, times(1)).toDTO(quete);
    }

    @Test
    @DisplayName("getQueteById - Doit lancer NotFoundException si l'ID n'existe pas")
    void getQueteById_ShouldThrowNotFoundException_WhenIdDoesNotExist() {
        when(queteRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> queteService.getQueteById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Quête non trouvée");

        verify(queteRepository, times(1)).findById(999L);
        verify(queteMapper, never()).toDTO(any());
    }

    // ========== Tests pour save() ==========

    @Test
    @DisplayName("save - Doit créer et sauvegarder une nouvelle quête")
    void save_ShouldCreateAndSaveNewQuete() {
        Quete newQuete = new Quete();
        newQuete.setNom("Nouvelle quête");
        newQuete.setDescription("Description de la quête");
        newQuete.setPrime(2000.0);
        newQuete.setStatut(StatutQuete.NOUVELLE);

        Quete savedQuete = new Quete();
        savedQuete.setId(2L);
        savedQuete.setNom("Nouvelle quête");
        savedQuete.setDescription("Description de la quête");
        savedQuete.setPrime(2000.0);
        savedQuete.setStatut(StatutQuete.NOUVELLE);

        QueteDTO savedQueteDTO = new QueteDTO();
        savedQueteDTO.setId(2L);
        savedQueteDTO.setNom("Nouvelle quête");
        savedQueteDTO.setStatut("NOUVELLE");

        when(queteMapper.toEntity(queteInputDTO)).thenReturn(newQuete);
        when(queteRepository.save(newQuete)).thenReturn(savedQuete);
        when(queteMapper.toDTO(savedQuete)).thenReturn(savedQueteDTO);

        QueteDTO result = queteService.save(queteInputDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getNom()).isEqualTo("Nouvelle quête");
        assertThat(result.getStatut()).isEqualTo("NOUVELLE");

        verify(queteMapper, times(1)).toEntity(queteInputDTO);
        verify(queteRepository, times(1)).save(newQuete);
        verify(queteMapper, times(1)).toDTO(savedQuete);
    }

    @Test
    @DisplayName("save - Doit définir le statut à NOUVELLE pour toute nouvelle quête")
    void save_ShouldSetStatusToNouvelle_ForNewQuete() {
        Quete newQuete = new Quete();
        newQuete.setNom("Test");
        newQuete.setStatut(StatutQuete.NOUVELLE);

        QueteDTO savedDTO = new QueteDTO();
        savedDTO.setId(1L);
        savedDTO.setStatut("NOUVELLE");

        when(queteMapper.toEntity(queteInputDTO)).thenReturn(newQuete);
        when(queteRepository.save(newQuete)).thenReturn(newQuete);
        when(queteMapper.toDTO(newQuete)).thenReturn(savedDTO);

        QueteDTO result = queteService.save(queteInputDTO);

        assertThat(result.getStatut()).isEqualTo("NOUVELLE");

        verify(queteMapper, times(1)).toEntity(queteInputDTO);
        verify(queteRepository, times(1)).save(newQuete);
    }

    // ========== Tests pour updateQuete() ==========

    @Test
    @DisplayName("updateQuete - Doit mettre à jour une quête existante")
    void updateQuete_ShouldUpdateExistingQuete() {
        QueteInputDTO updateDTO = new QueteInputDTO();
        updateDTO.setNom("Quête mise à jour");
        updateDTO.setDescription("Description mise à jour");
        updateDTO.setPrime(3000.0);
        updateDTO.setDureeEstimee(15);
        updateDTO.setDatePeremption(LocalDate.of(2027, 1, 1));

        Quete updatedQuete = new Quete();
        updatedQuete.setId(1L);
        updatedQuete.setNom("Quête mise à jour");
        updatedQuete.setDescription("Description mise à jour");
        updatedQuete.setPrime(3000.0);

        QueteDTO updatedDTO = new QueteDTO();
        updatedDTO.setId(1L);
        updatedDTO.setNom("Quête mise à jour");
        updatedDTO.setDescription("Description mise à jour");
        updatedDTO.setPrime(3000.0);

        when(queteRepository.findById(1L)).thenReturn(Optional.of(quete));
        doNothing().when(queteMapper).updateEntityFromDTO(updateDTO, quete);
        when(queteRepository.save(quete)).thenReturn(updatedQuete);
        when(queteMapper.toDTO(updatedQuete)).thenReturn(updatedDTO);

        QueteDTO result = queteService.updateQuete(1L, updateDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNom()).isEqualTo("Quête mise à jour");
        assertThat(result.getDescription()).isEqualTo("Description mise à jour");
        assertThat(result.getPrime()).isEqualTo(3000.0);

        verify(queteRepository, times(1)).findById(1L);
        verify(queteMapper, times(1)).updateEntityFromDTO(updateDTO, quete);
        verify(queteRepository, times(1)).save(quete);
        verify(queteMapper, times(1)).toDTO(updatedQuete);
    }

    @Test
    @DisplayName("updateQuete - Doit lancer NotFoundException si l'ID n'existe pas")
    void updateQuete_ShouldThrowNotFoundException_WhenIdDoesNotExist() {
        when(queteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> queteService.updateQuete(999L, queteInputDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Quête non trouvée");

        verify(queteRepository, times(1)).findById(999L);
        verify(queteMapper, never()).updateEntityFromDTO(any(), any());
        verify(queteRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateQuete - Doit permettre une mise à jour partielle")
    void updateQuete_ShouldAllowPartialUpdate() {
        QueteInputDTO partialUpdateDTO = new QueteInputDTO();
        partialUpdateDTO.setNom("Nom modifié");

        when(queteRepository.findById(1L)).thenReturn(Optional.of(quete));
        doNothing().when(queteMapper).updateEntityFromDTO(partialUpdateDTO, quete);
        when(queteRepository.save(quete)).thenReturn(quete);
        when(queteMapper.toDTO(quete)).thenReturn(queteDTO);

        QueteDTO result = queteService.updateQuete(1L, partialUpdateDTO);

        assertThat(result).isNotNull();

        verify(queteRepository, times(1)).findById(1L);
        verify(queteMapper, times(1)).updateEntityFromDTO(partialUpdateDTO, quete);
        verify(queteRepository, times(1)).save(quete);
    }

    // ========== Tests pour deleteQuete() ==========

    @Test
    @DisplayName("deleteQuete - Doit supprimer une quête existante")
    void deleteQuete_ShouldDeleteExistingQuete() {
        when(queteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(queteRepository).deleteById(1L);

        queteService.deleteQuete(1L);

        verify(queteRepository, times(1)).existsById(1L);
        verify(queteRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteQuete - Doit lancer NotFoundException si l'ID n'existe pas")
    void deleteQuete_ShouldThrowNotFoundException_WhenIdDoesNotExist() {
        when(queteRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> queteService.deleteQuete(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Quête non trouvée");

        verify(queteRepository, times(1)).existsById(999L);
        verify(queteRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deleteQuete - Doit vérifier l'existence avant de supprimer")
    void deleteQuete_ShouldCheckExistenceBeforeDeleting() {
        when(queteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(queteRepository).deleteById(1L);

        queteService.deleteQuete(1L);

        var inOrder = inOrder(queteRepository);
        inOrder.verify(queteRepository).existsById(1L);
        inOrder.verify(queteRepository).deleteById(1L);
    }
}
