package com.data.kanyh.mapper;

import com.data.kanyh.dto.EquipeDTO;
import com.data.kanyh.dto.EquipeInputDTO;
import com.data.kanyh.dto.ParticipationEquipeDTO;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.model.Equipe;
import com.data.kanyh.model.ParticipationEquipe;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("EquipeMapper - Tests unitaires")
class EquipeMapperTest {

    @Mock
    private ParticipationEquipeMapper participationEquipeMapper;

    @InjectMocks
    private EquipeMapper equipeMapper;

    private Equipe equipe;
    private Aventurier aventurier1;
    private Aventurier aventurier2;
    private ParticipationEquipe participation1;
    private ParticipationEquipe participation2;
    private ParticipationEquipeDTO participationDTO1;
    private ParticipationEquipeDTO participationDTO2;

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

        participation1 = new ParticipationEquipe();
        participation1.setId(1L);
        participation1.setEquipe(equipe);
        participation1.setAventurier(aventurier1);

        participation2 = new ParticipationEquipe();
        participation2.setId(2L);
        participation2.setEquipe(equipe);
        participation2.setAventurier(aventurier2);

        equipe.setParticipations(Arrays.asList(participation1, participation2));

        participationDTO1 = new ParticipationEquipeDTO();
        participationDTO1.setId(1L);
        participationDTO1.setAventurierNom("Gandalf");

        participationDTO2 = new ParticipationEquipeDTO();
        participationDTO2.setId(2L);
        participationDTO2.setAventurierNom("Aragorn");
    }

    @Test
    @DisplayName("toDTO - Doit convertir une entité Equipe en EquipeDTO avec tous les champs")
    void toDTO_ShouldConvertEntityToDTO_WithAllFields() {
        when(participationEquipeMapper.toDTO(participation1)).thenReturn(participationDTO1);
        when(participationEquipeMapper.toDTO(participation2)).thenReturn(participationDTO2);

        EquipeDTO dto = equipeMapper.toDTO(equipe);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNom()).isEqualTo("Les Compagnons");
        assertThat(dto.getDateDepart()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(dto.getDateRetourPrevue()).isEqualTo(LocalDate.of(2025, 2, 1));
        assertThat(dto.getCoutTotal()).isEqualTo(10000.0);
        assertThat(dto.getRatioRentabilite()).isEqualTo(1.5);
        assertThat(dto.getParticipations()).hasSize(2);
        assertThat(dto.getParticipations().get(0).getAventurierNom()).isEqualTo("Gandalf");
        assertThat(dto.getParticipations().get(1).getAventurierNom()).isEqualTo("Aragorn");
    }

    @Test
    @DisplayName("toDTO - Doit gérer une équipe sans participations")
    void toDTO_ShouldHandleEmptyParticipations() {
        equipe.setParticipations(List.of());

        EquipeDTO dto = equipeMapper.toDTO(equipe);

        assertThat(dto).isNotNull();
        assertThat(dto.getParticipations()).isEmpty();
    }

    @Test
    @DisplayName("toEntity - Doit convertir un EquipeInputDTO en entité Equipe")
    void toEntity_ShouldConvertInputDTOToEntity() {
        EquipeInputDTO inputDTO = new EquipeInputDTO();
        inputDTO.setNom("Nouvelle Équipe");

        Equipe result = equipeMapper.toEntity(inputDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull(); // L'ID n'est pas défini lors de la création
        assertThat(result.getNom()).isEqualTo("Nouvelle Équipe");
        assertThat(result.getParticipations()).isEmpty();
        assertThat(result.getDateDepart()).isNull();
        assertThat(result.getDateRetourPrevue()).isNull();
        assertThat(result.getCoutTotal()).isNull();
        assertThat(result.getRatioRentabilite()).isNull();
    }

    @Test
    @DisplayName("toEntity - Doit créer une équipe sans participations initialement")
    void toEntity_ShouldCreateEquipeWithoutParticipations() {
        EquipeInputDTO inputDTO = new EquipeInputDTO();
        inputDTO.setNom("Équipe Solo");

        Equipe result = equipeMapper.toEntity(inputDTO);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Équipe Solo");
        assertThat(result.getParticipations()).isEmpty();
    }

    @Test
    @DisplayName("updateEntityFromDTO - Doit mettre à jour uniquement le nom")
    void updateEntityFromDTO_ShouldUpdateOnlyName() {
        EquipeInputDTO updateDTO = new EquipeInputDTO();
        updateDTO.setNom("Nom Mis à Jour");

        Equipe existingEquipe = new Equipe();
        existingEquipe.setId(1L);
        existingEquipe.setNom("Ancien Nom");
        existingEquipe.setDateDepart(LocalDate.of(2025, 1, 1));
        existingEquipe.setCoutTotal(5000.0);
        existingEquipe.setParticipations(Arrays.asList(participation1, participation2));

        equipeMapper.updateEntityFromDTO(updateDTO, existingEquipe);

        assertThat(existingEquipe.getNom()).isEqualTo("Nom Mis à Jour");
        assertThat(existingEquipe.getParticipations()).hasSize(2);
        assertThat(existingEquipe.getId()).isEqualTo(1L);
        assertThat(existingEquipe.getDateDepart()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(existingEquipe.getCoutTotal()).isEqualTo(5000.0);
    }

    @Test
    @DisplayName("updateEntityFromDTO - Ne doit pas modifier les participations")
    void updateEntityFromDTO_ShouldNotModifyParticipations() {
        EquipeInputDTO updateDTO = new EquipeInputDTO();
        updateDTO.setNom("Équipe");

        Equipe existingEquipe = new Equipe();
        existingEquipe.setParticipations(Arrays.asList(participation1));

        equipeMapper.updateEntityFromDTO(updateDTO, existingEquipe);

        assertThat(existingEquipe.getParticipations()).hasSize(1);
        assertThat(existingEquipe.getParticipations().get(0).getAventurier().getNom()).isEqualTo("Gandalf");
    }
}
