package com.data.kanyh.service;

import com.data.kanyh.dto.AventurierDTO;
import com.data.kanyh.dto.ParticipationEquipeDTO;
import com.data.kanyh.dto.ParticipationEquipeInputDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.AventurierMapper;
import com.data.kanyh.mapper.ParticipationEquipeMapper;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.model.Equipe;
import com.data.kanyh.model.ParticipationEquipe;
import com.data.kanyh.repository.AventurierRepository;
import com.data.kanyh.repository.EquipeRepository;
import com.data.kanyh.repository.ParticipationEquipeRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ParticipationEquipeService - Tests unitaires")
class ParticipationEquipeServiceTest {

    @Mock
    private ParticipationEquipeRepository participationEquipeRepository;

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private AventurierRepository aventurierRepository;

    @Mock
    private ParticipationEquipeMapper participationEquipeMapper;

    @Mock
    private AventurierMapper aventurierMapper;

    @InjectMocks
    private ParticipationEquipeService participationEquipeService;

    private Equipe equipe;
    private Aventurier aventurier;
    private ParticipationEquipe participation;
    private ParticipationEquipeInputDTO inputDTO;
    private ParticipationEquipeDTO participationDTO;

    @BeforeEach
    void setUp() {
        equipe = new Equipe();
        equipe.setId(1L);
        equipe.setNom("Les Braves");

        aventurier = new Aventurier();
        aventurier.setId(2L);
        aventurier.setNom("Gandalf");

        participation = new ParticipationEquipe();
        participation.setId(10L);
        participation.setEquipe(equipe);
        participation.setAventurier(aventurier);
        participation.setDateAffectation(LocalDate.of(2025, 11, 1));
        participation.setDateRetour(LocalDate.of(2025, 11, 15));
        participation.setEtat("en_attente");
        participation.setGainExperience(0);

        inputDTO = new ParticipationEquipeInputDTO();
        inputDTO.setEquipeId(1L);
        inputDTO.setAventurierId(2L);
        inputDTO.setDateAffectation(LocalDate.of(2025, 11, 1));
        inputDTO.setDateRetour(LocalDate.of(2025, 11, 15));
        inputDTO.setEtat("en_attente");
        inputDTO.setGainExperience(0);

        participationDTO = new ParticipationEquipeDTO();
        participationDTO.setId(10L);
        participationDTO.setEquipeId(1L);
        participationDTO.setEquipeNom("Les Braves");
        participationDTO.setAventurierId(2L);
        participationDTO.setAventurierNom("Gandalf");
        participationDTO.setDateAffectation(LocalDate.of(2025, 11, 1));
        participationDTO.setDateRetour(LocalDate.of(2025, 11, 15));
        participationDTO.setEtat("en_attente");
        participationDTO.setGainExperience(0);
    }

    // ========== Tests pour createParticipation() ==========

    @Test
    @DisplayName("createParticipation - Doit créer une participation avec succès")
    void createParticipation_ShouldCreateSuccessfully() {
        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));
        when(aventurierRepository.findById(2L)).thenReturn(Optional.of(aventurier));
        when(participationEquipeMapper.toEntity(inputDTO, equipe, aventurier)).thenReturn(participation);
        when(participationEquipeRepository.save(participation)).thenReturn(participation);
        when(participationEquipeMapper.toDTO(participation)).thenReturn(participationDTO);

        ParticipationEquipeDTO result = participationEquipeService.createParticipation(inputDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getEquipeId()).isEqualTo(1L);
        assertThat(result.getAventurierId()).isEqualTo(2L);

        verify(equipeRepository, times(1)).findById(1L);
        verify(aventurierRepository, times(1)).findById(2L);
        verify(participationEquipeMapper, times(1)).toEntity(inputDTO, equipe, aventurier);
        verify(participationEquipeRepository, times(1)).save(participation);
        verify(participationEquipeMapper, times(1)).toDTO(participation);
    }

    @Test
    @DisplayName("createParticipation - Doit lancer NotFoundException si l'équipe n'existe pas")
    void createParticipation_ShouldThrowNotFoundException_WhenEquipeNotFound() {
        when(equipeRepository.findById(999L)).thenReturn(Optional.empty());
        inputDTO.setEquipeId(999L);

        assertThatThrownBy(() -> participationEquipeService.createParticipation(inputDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Équipe non trouvée");

        verify(equipeRepository, times(1)).findById(999L);
        verify(aventurierRepository, never()).findById(any());
        verify(participationEquipeRepository, never()).save(any());
    }

    @Test
    @DisplayName("createParticipation - Doit lancer NotFoundException si l'aventurier n'existe pas")
    void createParticipation_ShouldThrowNotFoundException_WhenAventurierNotFound() {
        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));
        when(aventurierRepository.findById(999L)).thenReturn(Optional.empty());
        inputDTO.setAventurierId(999L);

        assertThatThrownBy(() -> participationEquipeService.createParticipation(inputDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Aventurier non trouvé");

        verify(equipeRepository, times(1)).findById(1L);
        verify(aventurierRepository, times(1)).findById(999L);
        verify(participationEquipeRepository, never()).save(any());
    }

    @Test
    @DisplayName("createParticipation - Doit créer une participation sans dateRetour")
    void createParticipation_ShouldCreateParticipation_WithoutDateRetour() {
        inputDTO.setDateRetour(null);
        participation.setDateRetour(null);
        participationDTO.setDateRetour(null);

        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));
        when(aventurierRepository.findById(2L)).thenReturn(Optional.of(aventurier));
        when(participationEquipeMapper.toEntity(inputDTO, equipe, aventurier)).thenReturn(participation);
        when(participationEquipeRepository.save(participation)).thenReturn(participation);
        when(participationEquipeMapper.toDTO(participation)).thenReturn(participationDTO);

        ParticipationEquipeDTO result = participationEquipeService.createParticipation(inputDTO);

        assertThat(result).isNotNull();
        assertThat(result.getDateRetour()).isNull();
    }

    // ========== Tests pour getParticipationsByEquipe() ==========

    @Test
    @DisplayName("getParticipationsByEquipe - Doit retourner toutes les participations d'une équipe")
    void getParticipationsByEquipe_ShouldReturnAllParticipations() {
        ParticipationEquipe participation2 = new ParticipationEquipe();
        participation2.setId(11L);
        participation2.setEquipe(equipe);

        Aventurier aventurier2 = new Aventurier();
        aventurier2.setId(3L);
        aventurier2.setNom("Aragorn");
        participation2.setAventurier(aventurier2);

        ParticipationEquipeDTO participationDTO2 = new ParticipationEquipeDTO();
        participationDTO2.setId(11L);
        participationDTO2.setAventurierNom("Aragorn");

        List<ParticipationEquipe> participations = Arrays.asList(participation, participation2);

        when(participationEquipeRepository.findByEquipeId(1L)).thenReturn(participations);
        when(participationEquipeMapper.toDTO(participation)).thenReturn(participationDTO);
        when(participationEquipeMapper.toDTO(participation2)).thenReturn(participationDTO2);

        List<ParticipationEquipeDTO> result = participationEquipeService.getParticipationsByEquipe(1L);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(10L);
        assertThat(result.get(1).getId()).isEqualTo(11L);

        verify(participationEquipeRepository, times(1)).findByEquipeId(1L);
        verify(participationEquipeMapper, times(2)).toDTO(any());
    }

    @Test
    @DisplayName("getParticipationsByEquipe - Doit retourner une liste vide si aucune participation")
    void getParticipationsByEquipe_ShouldReturnEmptyList_WhenNoParticipations() {
        when(participationEquipeRepository.findByEquipeId(999L)).thenReturn(List.of());

        List<ParticipationEquipeDTO> result = participationEquipeService.getParticipationsByEquipe(999L);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(participationEquipeRepository, times(1)).findByEquipeId(999L);
        verify(participationEquipeMapper, never()).toDTO(any());
    }

    // ========== Tests pour getParticipationsByAventurier() ==========

    @Test
    @DisplayName("getParticipationsByAventurier - Doit retourner toutes les participations d'un aventurier")
    void getParticipationsByAventurier_ShouldReturnAllParticipations() {
        Equipe equipe2 = new Equipe();
        equipe2.setId(2L);
        equipe2.setNom("Les Gardiens");

        ParticipationEquipe participation2 = new ParticipationEquipe();
        participation2.setId(12L);
        participation2.setEquipe(equipe2);
        participation2.setAventurier(aventurier);

        ParticipationEquipeDTO participationDTO2 = new ParticipationEquipeDTO();
        participationDTO2.setId(12L);
        participationDTO2.setEquipeNom("Les Gardiens");

        List<ParticipationEquipe> participations = Arrays.asList(participation, participation2);

        when(participationEquipeRepository.findByAventurierId(2L)).thenReturn(participations);
        when(participationEquipeMapper.toDTO(participation)).thenReturn(participationDTO);
        when(participationEquipeMapper.toDTO(participation2)).thenReturn(participationDTO2);

        List<ParticipationEquipeDTO> result = participationEquipeService.getParticipationsByAventurier(2L);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(10L);
        assertThat(result.get(1).getId()).isEqualTo(12L);

        verify(participationEquipeRepository, times(1)).findByAventurierId(2L);
        verify(participationEquipeMapper, times(2)).toDTO(any());
    }

    @Test
    @DisplayName("getParticipationsByAventurier - Doit retourner une liste vide si aucune participation")
    void getParticipationsByAventurier_ShouldReturnEmptyList_WhenNoParticipations() {
        when(participationEquipeRepository.findByAventurierId(999L)).thenReturn(List.of());

        List<ParticipationEquipeDTO> result = participationEquipeService.getParticipationsByAventurier(999L);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(participationEquipeRepository, times(1)).findByAventurierId(999L);
        verify(participationEquipeMapper, never()).toDTO(any());
    }

    // ========== Tests pour getAventuriersDisponibles() ==========

    @Test
    @DisplayName("getAventuriersDisponibles - Doit retourner tous les aventuriers si aucune participation")
    void getAventuriersDisponibles_ShouldReturnAllAventuriers_WhenNoParticipations() {
        Aventurier aventurier2 = new Aventurier();
        aventurier2.setId(3L);
        aventurier2.setNom("Aragorn");

        List<Aventurier> allAventuriers = Arrays.asList(aventurier, aventurier2);
        LocalDate dateDebut = LocalDate.of(2025, 12, 1);
        LocalDate dateFin = LocalDate.of(2025, 12, 31);

        when(aventurierRepository.findAll()).thenReturn(allAventuriers);
        when(participationEquipeRepository.findParticipationsByAventurierAndDateRange(2L, dateDebut, dateFin))
                .thenReturn(List.of());
        when(participationEquipeRepository.findParticipationsByAventurierAndDateRange(3L, dateDebut, dateFin))
                .thenReturn(List.of());

        AventurierDTO aventurierDTO1 = new AventurierDTO();
        aventurierDTO1.setId(2L);
        AventurierDTO aventurierDTO2 = new AventurierDTO();
        aventurierDTO2.setId(3L);

        when(aventurierMapper.toDTO(aventurier)).thenReturn(aventurierDTO1);
        when(aventurierMapper.toDTO(aventurier2)).thenReturn(aventurierDTO2);

        List<AventurierDTO> result = participationEquipeService.getAventuriersDisponibles(dateDebut, dateFin);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        verify(aventurierRepository, times(1)).findAll();
        verify(participationEquipeRepository, times(2)).findParticipationsByAventurierAndDateRange(any(), any(), any());
    }

    @Test
    @DisplayName("getAventuriersDisponibles - Doit exclure les aventuriers occupés")
    void getAventuriersDisponibles_ShouldExcludeOccupiedAventuriers() {
        Aventurier aventurier2 = new Aventurier();
        aventurier2.setId(3L);
        aventurier2.setNom("Aragorn");

        Aventurier aventurier3 = new Aventurier();
        aventurier3.setId(4L);
        aventurier3.setNom("Legolas");

        List<Aventurier> allAventuriers = Arrays.asList(aventurier, aventurier2, aventurier3);
        LocalDate dateDebut = LocalDate.of(2025, 12, 1);
        LocalDate dateFin = LocalDate.of(2025, 12, 31);

        ParticipationEquipe participationOccupee = new ParticipationEquipe();
        participationOccupee.setAventurier(aventurier);
        participationOccupee.setDateAffectation(LocalDate.of(2025, 12, 5));

        when(aventurierRepository.findAll()).thenReturn(allAventuriers);
        when(participationEquipeRepository.findParticipationsByAventurierAndDateRange(2L, dateDebut, dateFin))
                .thenReturn(Arrays.asList(participationOccupee)); // Gandalf est occupé
        when(participationEquipeRepository.findParticipationsByAventurierAndDateRange(3L, dateDebut, dateFin))
                .thenReturn(List.of()); // Aragorn est disponible
        when(participationEquipeRepository.findParticipationsByAventurierAndDateRange(4L, dateDebut, dateFin))
                .thenReturn(List.of()); // Legolas est disponible

        AventurierDTO aventurierDTO2 = new AventurierDTO();
        aventurierDTO2.setId(3L);
        AventurierDTO aventurierDTO3 = new AventurierDTO();
        aventurierDTO3.setId(4L);

        when(aventurierMapper.toDTO(aventurier2)).thenReturn(aventurierDTO2);
        when(aventurierMapper.toDTO(aventurier3)).thenReturn(aventurierDTO3);

        List<AventurierDTO> result = participationEquipeService.getAventuriersDisponibles(dateDebut, dateFin);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(AventurierDTO::getId).containsExactlyInAnyOrder(3L, 4L);
        assertThat(result).extracting(AventurierDTO::getId).doesNotContain(2L);

        verify(aventurierRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAventuriersDisponibles - Doit retourner une liste vide si tous sont occupés")
    void getAventuriersDisponibles_ShouldReturnEmptyList_WhenAllAreOccupied() {
        List<Aventurier> allAventuriers = Arrays.asList(aventurier);
        LocalDate dateDebut = LocalDate.of(2025, 12, 1);
        LocalDate dateFin = LocalDate.of(2025, 12, 31);

        ParticipationEquipe participationOccupee = new ParticipationEquipe();
        participationOccupee.setAventurier(aventurier);

        when(aventurierRepository.findAll()).thenReturn(allAventuriers);
        when(participationEquipeRepository.findParticipationsByAventurierAndDateRange(2L, dateDebut, dateFin))
                .thenReturn(Arrays.asList(participationOccupee));

        List<AventurierDTO> result = participationEquipeService.getAventuriersDisponibles(dateDebut, dateFin);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(aventurierRepository, times(1)).findAll();
        verify(aventurierMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("getAventuriersDisponibles - Doit retourner une liste vide si aucun aventurier n'existe")
    void getAventuriersDisponibles_ShouldReturnEmptyList_WhenNoAventuriersExist() {
        LocalDate dateDebut = LocalDate.of(2025, 12, 1);
        LocalDate dateFin = LocalDate.of(2025, 12, 31);

        when(aventurierRepository.findAll()).thenReturn(List.of());

        List<AventurierDTO> result = participationEquipeService.getAventuriersDisponibles(dateDebut, dateFin);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(aventurierRepository, times(1)).findAll();
        verify(participationEquipeRepository, never()).findParticipationsByAventurierAndDateRange(any(), any(), any());
    }
}
