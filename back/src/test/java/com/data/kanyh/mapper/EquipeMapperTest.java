package com.data.kanyh.mapper;

import com.data.kanyh.dto.AventurierDTO;
import com.data.kanyh.dto.EquipeDTO;
import com.data.kanyh.dto.EquipeInputDTO;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.model.Equipe;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("EquipeMapper - Tests unitaires")
class EquipeMapperTest {

    @Mock
    private AventurierMapper aventurierMapper;

    @InjectMocks
    private EquipeMapper equipeMapper;

    private Equipe equipe;
    private Aventurier aventurier1;
    private Aventurier aventurier2;
    private AventurierDTO aventurierDTO1;
    private AventurierDTO aventurierDTO2;

    @BeforeEach
    void setUp() {
        aventurier1 = new Aventurier();
        aventurier1.setId(1L);
        aventurier1.setNom("Gandalf");
        aventurier1.setSpecialite("Mage");

        aventurierDTO1 = new AventurierDTO();
        aventurierDTO1.setId(1L);
        aventurierDTO1.setNom("Gandalf");
        aventurierDTO1.setSpecialite("Mage");

        aventurier2 = new Aventurier();
        aventurier2.setId(2L);
        aventurier2.setNom("Aragorn");
        aventurier2.setSpecialite("Guerrier");

        aventurierDTO2 = new AventurierDTO();
        aventurierDTO2.setId(2L);
        aventurierDTO2.setNom("Aragorn");
        aventurierDTO2.setSpecialite("Guerrier");

        equipe = new Equipe();
        equipe.setId(1L);
        equipe.setNom("Les Compagnons");
        equipe.setDateDepart(LocalDate.of(2025, 1, 1));
        equipe.setDateRetourPrevue(LocalDate.of(2025, 2, 1));
        equipe.setCoutTotal(10000.0);
        equipe.setRatioRentabilite(1.5);
        equipe.setAventuriers(Arrays.asList(aventurier1, aventurier2));
    }

    @Test
    @DisplayName("toDTO - Doit convertir une entité Equipe en EquipeDTO avec tous les champs")
    void toDTO_ShouldConvertEntityToDTO_WithAllFields() {
        when(aventurierMapper.toDTO(aventurier1)).thenReturn(aventurierDTO1);
        when(aventurierMapper.toDTO(aventurier2)).thenReturn(aventurierDTO2);

        EquipeDTO dto = equipeMapper.toDTO(equipe);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNom()).isEqualTo("Les Compagnons");
        assertThat(dto.getDateDepart()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(dto.getDateRetourPrevue()).isEqualTo(LocalDate.of(2025, 2, 1));
        assertThat(dto.getCoutTotal()).isEqualTo(10000.0);
        assertThat(dto.getRatioRentabilite()).isEqualTo(1.5);
        assertThat(dto.getAventuriers()).hasSize(2);
        assertThat(dto.getAventuriers().get(0).getNom()).isEqualTo("Gandalf");
        assertThat(dto.getAventuriers().get(1).getNom()).isEqualTo("Aragorn");
    }

    @Test
    @DisplayName("toDTO - Doit gérer une équipe sans aventuriers")
    void toDTO_ShouldHandleEmptyAventuriers() {
        equipe.setAventuriers(List.of());

        EquipeDTO dto = equipeMapper.toDTO(equipe);

        assertThat(dto).isNotNull();
        assertThat(dto.getAventuriers()).isEmpty();
    }

    @Test
    @DisplayName("toEntity - Doit convertir un EquipeInputDTO en entité Equipe")
    void toEntity_ShouldConvertInputDTOToEntity() {
        EquipeInputDTO inputDTO = new EquipeInputDTO();
        inputDTO.setNom("Nouvelle Équipe");
        inputDTO.setAventuriers(Arrays.asList(1L, 2L));

        List<Aventurier> aventuriers = Arrays.asList(aventurier1, aventurier2);

        Equipe result = equipeMapper.toEntity(inputDTO, aventuriers);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull(); // L'ID n'est pas défini lors de la création
        assertThat(result.getNom()).isEqualTo("Nouvelle Équipe");
        assertThat(result.getAventuriers()).hasSize(2);
        assertThat(result.getAventuriers().get(0).getNom()).isEqualTo("Gandalf");
        assertThat(result.getAventuriers().get(1).getNom()).isEqualTo("Aragorn");
        assertThat(result.getDateDepart()).isNull();
        assertThat(result.getDateRetourPrevue()).isNull();
        assertThat(result.getCoutTotal()).isNull();
        assertThat(result.getRatioRentabilite()).isNull();
    }

    @Test
    @DisplayName("toEntity - Doit créer une équipe avec un seul aventurier")
    void toEntity_ShouldCreateEquipeWithSingleAventurier() {
        EquipeInputDTO inputDTO = new EquipeInputDTO();
        inputDTO.setNom("Équipe Solo");
        inputDTO.setAventuriers(List.of(1L));

        List<Aventurier> aventuriers = List.of(aventurier1);

        Equipe result = equipeMapper.toEntity(inputDTO, aventuriers);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Équipe Solo");
        assertThat(result.getAventuriers()).hasSize(1);
        assertThat(result.getAventuriers().get(0).getNom()).isEqualTo("Gandalf");
    }

    @Test
    @DisplayName("updateEntityFromDTO - Doit mettre à jour le nom et les aventuriers")
    void updateEntityFromDTO_ShouldUpdateNameAndAventuriers() {
        EquipeInputDTO updateDTO = new EquipeInputDTO();
        updateDTO.setNom("Nom Mis à Jour");
        updateDTO.setAventuriers(List.of(1L));

        List<Aventurier> newAventuriers = List.of(aventurier1);

        Equipe existingEquipe = new Equipe();
        existingEquipe.setId(1L);
        existingEquipe.setNom("Ancien Nom");
        existingEquipe.setDateDepart(LocalDate.of(2025, 1, 1));
        existingEquipe.setCoutTotal(5000.0);
        existingEquipe.setAventuriers(Arrays.asList(aventurier1, aventurier2));

        equipeMapper.updateEntityFromDTO(updateDTO, existingEquipe, newAventuriers);

        assertThat(existingEquipe.getNom()).isEqualTo("Nom Mis à Jour");
        assertThat(existingEquipe.getAventuriers()).hasSize(1);
        assertThat(existingEquipe.getAventuriers().get(0).getNom()).isEqualTo("Gandalf");
        assertThat(existingEquipe.getId()).isEqualTo(1L);
        assertThat(existingEquipe.getDateDepart()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(existingEquipe.getCoutTotal()).isEqualTo(5000.0);
    }

    @Test
    @DisplayName("updateEntityFromDTO - Doit remplacer complètement la liste d'aventuriers")
    void updateEntityFromDTO_ShouldReplaceAventuriersList() {
        EquipeInputDTO updateDTO = new EquipeInputDTO();
        updateDTO.setNom("Équipe");
        updateDTO.setAventuriers(Arrays.asList(2L));

        List<Aventurier> newAventuriers = List.of(aventurier2);

        Equipe existingEquipe = new Equipe();
        existingEquipe.setAventuriers(Arrays.asList(aventurier1));

        equipeMapper.updateEntityFromDTO(updateDTO, existingEquipe, newAventuriers);

        assertThat(existingEquipe.getAventuriers()).hasSize(1);
        assertThat(existingEquipe.getAventuriers().get(0).getNom()).isEqualTo("Aragorn");
    }
}
