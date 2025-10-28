package com.data.kanyh.service;

import com.data.kanyh.dto.EquipeDTO;
import com.data.kanyh.dto.EquipeInputDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.EquipeMapper;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.model.Equipe;
import com.data.kanyh.repository.EquipeRepository;
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
@DisplayName("EquipeService - Tests unitaires")
class EquipeServiceTest {

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private EquipeMapper equipeMapper;

    @InjectMocks
    private EquipeService equipeService;

    private Equipe equipe;
    private EquipeDTO equipeDTO;
    private EquipeInputDTO equipeInputDTO;
    private Aventurier aventurier1;
    private Aventurier aventurier2;

    @BeforeEach
    void setUp() {
        aventurier1 = new Aventurier();
        aventurier1.setId(1L);
        aventurier1.setNom("Gandalf");

        aventurier2 = new Aventurier();
        aventurier2.setId(2L);
        aventurier2.setNom("Aragorn");

        equipe = new Equipe();
        equipe.setId(1L);
        equipe.setNom("Les Compagnons");
        equipe.setDateDepart(LocalDate.of(2025, 1, 1));
        equipe.setDateRetourPrevue(LocalDate.of(2025, 2, 1));
        equipe.setCoutTotal(10000.0);
        equipe.setRatioRentabilite(1.5);

        equipeDTO = new EquipeDTO();
        equipeDTO.setId(1L);
        equipeDTO.setNom("Les Compagnons");
        equipeDTO.setDateDepart(LocalDate.of(2025, 1, 1));
        equipeDTO.setDateRetourPrevue(LocalDate.of(2025, 2, 1));
        equipeDTO.setCoutTotal(10000.0);
        equipeDTO.setRatioRentabilite(1.5);

        equipeInputDTO = new EquipeInputDTO();
        equipeInputDTO.setNom("Nouvelle Équipe");
    }

    // ========== Tests pour getAllEquipes() ==========

    @Test
    @DisplayName("getAllEquipes - Doit retourner toutes les équipes")
    void getAllEquipes_ShouldReturnAllEquipes() {
        Equipe equipe2 = new Equipe();
        equipe2.setId(2L);
        equipe2.setNom("Les Gardiens");

        EquipeDTO equipeDTO2 = new EquipeDTO();
        equipeDTO2.setId(2L);
        equipeDTO2.setNom("Les Gardiens");

        List<Equipe> equipes = Arrays.asList(equipe, equipe2);

        when(equipeRepository.findAll()).thenReturn(equipes);
        when(equipeMapper.toDTO(equipe)).thenReturn(equipeDTO);
        when(equipeMapper.toDTO(equipe2)).thenReturn(equipeDTO2);

        List<EquipeDTO> result = equipeService.getAllEquipes();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getNom()).isEqualTo("Les Compagnons");
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getNom()).isEqualTo("Les Gardiens");

        verify(equipeRepository, times(1)).findAll();
        verify(equipeMapper, times(1)).toDTO(equipe);
        verify(equipeMapper, times(1)).toDTO(equipe2);
    }

    @Test
    @DisplayName("getAllEquipes - Doit retourner une liste vide si aucune équipe")
    void getAllEquipes_ShouldReturnEmptyList_WhenNoEquipes() {
        when(equipeRepository.findAll()).thenReturn(List.of());

        List<EquipeDTO> result = equipeService.getAllEquipes();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(equipeRepository, times(1)).findAll();
        verify(equipeMapper, never()).toDTO(any());
    }

    // ========== Tests pour getEquipeById() ==========

    @Test
    @DisplayName("getEquipeById - Doit retourner l'équipe correspondante")
    void getEquipeById_ShouldReturnEquipe_WhenIdExists() {
        // Given
        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));
        when(equipeMapper.toDTO(equipe)).thenReturn(equipeDTO);

        // When
        EquipeDTO result = equipeService.getEquipeById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNom()).isEqualTo("Les Compagnons");

        verify(equipeRepository, times(1)).findById(1L);
        verify(equipeMapper, times(1)).toDTO(equipe);
    }

    @Test
    @DisplayName("getEquipeById - Doit lancer NotFoundException si l'ID n'existe pas")
    void getEquipeById_ShouldThrowNotFoundException_WhenIdDoesNotExist() {
        when(equipeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> equipeService.getEquipeById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Équipe non trouvée");

        verify(equipeRepository, times(1)).findById(999L);
        verify(equipeMapper, never()).toDTO(any());
    }

    // ========== Tests pour save() ==========

    @Test
    @DisplayName("save - Doit créer et sauvegarder une nouvelle équipe")
    void save_ShouldCreateAndSaveNewEquipe() {
        Equipe newEquipe = new Equipe();
        newEquipe.setNom("Nouvelle Équipe");

        Equipe savedEquipe = new Equipe();
        savedEquipe.setId(2L);
        savedEquipe.setNom("Nouvelle Équipe");

        EquipeDTO savedEquipeDTO = new EquipeDTO();
        savedEquipeDTO.setId(2L);
        savedEquipeDTO.setNom("Nouvelle Équipe");

        when(equipeMapper.toEntity(equipeInputDTO)).thenReturn(newEquipe);
        when(equipeRepository.save(newEquipe)).thenReturn(savedEquipe);
        when(equipeMapper.toDTO(savedEquipe)).thenReturn(savedEquipeDTO);

        EquipeDTO result = equipeService.save(equipeInputDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getNom()).isEqualTo("Nouvelle Équipe");

        verify(equipeMapper, times(1)).toEntity(equipeInputDTO);
        verify(equipeRepository, times(1)).save(newEquipe);
        verify(equipeMapper, times(1)).toDTO(savedEquipe);
    }

    // ========== Tests pour updateEquipe() ==========

    @Test
    @DisplayName("updateEquipe - Doit mettre à jour une équipe existante")
    void updateEquipe_ShouldUpdateExistingEquipe() {
        EquipeInputDTO updateDTO = new EquipeInputDTO();
        updateDTO.setNom("Équipe Mise à Jour");

        Equipe updatedEquipe = new Equipe();
        updatedEquipe.setId(1L);
        updatedEquipe.setNom("Équipe Mise à Jour");

        EquipeDTO updatedDTO = new EquipeDTO();
        updatedDTO.setId(1L);
        updatedDTO.setNom("Équipe Mise à Jour");

        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));
        doNothing().when(equipeMapper).updateEntityFromDTO(updateDTO, equipe);
        when(equipeRepository.save(equipe)).thenReturn(updatedEquipe);
        when(equipeMapper.toDTO(updatedEquipe)).thenReturn(updatedDTO);

        EquipeDTO result = equipeService.updateEquipe(1L, updateDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNom()).isEqualTo("Équipe Mise à Jour");

        verify(equipeRepository, times(1)).findById(1L);
        verify(equipeMapper, times(1)).updateEntityFromDTO(updateDTO, equipe);
        verify(equipeRepository, times(1)).save(equipe);
        verify(equipeMapper, times(1)).toDTO(updatedEquipe);
    }

    @Test
    @DisplayName("updateEquipe - Doit lancer NotFoundException si l'équipe n'existe pas")
    void updateEquipe_ShouldThrowNotFoundException_WhenEquipeDoesNotExist() {
        when(equipeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> equipeService.updateEquipe(999L, equipeInputDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Équipe non trouvée");

        verify(equipeRepository, times(1)).findById(999L);
        verify(equipeMapper, never()).updateEntityFromDTO(any(), any());
        verify(equipeRepository, never()).save(any());
    }

    // ========== Tests pour deleteEquipe() ==========

    @Test
    @DisplayName("deleteEquipe - Doit supprimer une équipe existante")
    void deleteEquipe_ShouldDeleteExistingEquipe() {
        when(equipeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(equipeRepository).deleteById(1L);

        equipeService.deleteEquipe(1L);

        verify(equipeRepository, times(1)).existsById(1L);
        verify(equipeRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteEquipe - Doit lancer NotFoundException si l'ID n'existe pas")
    void deleteEquipe_ShouldThrowNotFoundException_WhenIdDoesNotExist() {
        when(equipeRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> equipeService.deleteEquipe(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Équipe non trouvée");

        verify(equipeRepository, times(1)).existsById(999L);
        verify(equipeRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deleteEquipe - Doit vérifier l'existence avant de supprimer")
    void deleteEquipe_ShouldCheckExistenceBeforeDeleting() {
        when(equipeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(equipeRepository).deleteById(1L);

        equipeService.deleteEquipe(1L);

        var inOrder = inOrder(equipeRepository);
        inOrder.verify(equipeRepository).existsById(1L);
        inOrder.verify(equipeRepository).deleteById(1L);
    }
}
