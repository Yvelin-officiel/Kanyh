package com.data.kanyh.mapper;

import com.data.kanyh.dto.ParticipationEquipeDTO;
import com.data.kanyh.dto.ParticipationEquipeInputDTO;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.model.Equipe;
import com.data.kanyh.model.ParticipationEquipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ParticipationEquipeMapper - Tests unitaires")
class ParticipationEquipeMapperTest {

    private ParticipationEquipeMapper mapper;
    private Equipe equipe;
    private Aventurier aventurier;
    private ParticipationEquipe participation;

    @BeforeEach
    void setUp() {
        mapper = new ParticipationEquipeMapper();

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
        participation.setEtat("en_cours");
        participation.setGainExperience(100);
    }

    // ========== Tests pour toDTO() ==========

    @Test
    @DisplayName("toDTO - Doit convertir une ParticipationEquipe en DTO avec tous les champs")
    void toDTO_ShouldConvertEntityToDTO_WithAllFields() {
        ParticipationEquipeDTO dto = mapper.toDTO(participation);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getEquipeId()).isEqualTo(1L);
        assertThat(dto.getEquipeNom()).isEqualTo("Les Braves");
        assertThat(dto.getAventurierId()).isEqualTo(2L);
        assertThat(dto.getAventurierNom()).isEqualTo("Gandalf");
        assertThat(dto.getDateAffectation()).isEqualTo(LocalDate.of(2025, 11, 1));
        assertThat(dto.getDateRetour()).isEqualTo(LocalDate.of(2025, 11, 15));
        assertThat(dto.getEtat()).isEqualTo("en_cours");
        assertThat(dto.getGainExperience()).isEqualTo(100);
    }

    @Test
    @DisplayName("toDTO - Doit gérer les champs optionnels null (dateRetour)")
    void toDTO_ShouldHandleNullOptionalFields() {
        participation.setDateRetour(null);
        participation.setEtat(null);
        participation.setGainExperience(null);

        ParticipationEquipeDTO dto = mapper.toDTO(participation);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getDateRetour()).isNull();
        assertThat(dto.getEtat()).isNull();
        assertThat(dto.getGainExperience()).isNull();
    }

    @Test
    @DisplayName("toDTO - Doit gérer une participation avec état en_attente")
    void toDTO_ShouldHandleEnAttenteState() {
        participation.setEtat("en_attente");
        participation.setGainExperience(0);

        ParticipationEquipeDTO dto = mapper.toDTO(participation);

        assertThat(dto).isNotNull();
        assertThat(dto.getEtat()).isEqualTo("en_attente");
        assertThat(dto.getGainExperience()).isEqualTo(0);
    }

    @Test
    @DisplayName("toDTO - Doit gérer une participation avec état termine")
    void toDTO_ShouldHandleTermineState() {
        participation.setEtat("termine");
        participation.setGainExperience(500);
        participation.setDateRetour(LocalDate.of(2025, 11, 20));

        ParticipationEquipeDTO dto = mapper.toDTO(participation);

        assertThat(dto).isNotNull();
        assertThat(dto.getEtat()).isEqualTo("termine");
        assertThat(dto.getGainExperience()).isEqualTo(500);
        assertThat(dto.getDateRetour()).isEqualTo(LocalDate.of(2025, 11, 20));
    }

    @Test
    @DisplayName("toDTO - Doit gérer une participation avec état annule")
    void toDTO_ShouldHandleAnnuleState() {
        participation.setEtat("annule");
        participation.setGainExperience(0);

        ParticipationEquipeDTO dto = mapper.toDTO(participation);

        assertThat(dto).isNotNull();
        assertThat(dto.getEtat()).isEqualTo("annule");
    }

    // ========== Tests pour toEntity() ==========

    @Test
    @DisplayName("toEntity - Doit convertir un InputDTO en entité avec tous les champs")
    void toEntity_ShouldConvertInputDTOToEntity_WithAllFields() {
        ParticipationEquipeInputDTO inputDTO = new ParticipationEquipeInputDTO();
        inputDTO.setEquipeId(1L);
        inputDTO.setAventurierId(2L);
        inputDTO.setDateAffectation(LocalDate.of(2025, 12, 1));
        inputDTO.setDateRetour(LocalDate.of(2025, 12, 31));
        inputDTO.setEtat("en_attente");
        inputDTO.setGainExperience(0);

        ParticipationEquipe entity = mapper.toEntity(inputDTO, equipe, aventurier);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull(); // L'ID n'est pas défini lors de la création
        assertThat(entity.getEquipe()).isEqualTo(equipe);
        assertThat(entity.getAventurier()).isEqualTo(aventurier);
        assertThat(entity.getDateAffectation()).isEqualTo(LocalDate.of(2025, 12, 1));
        assertThat(entity.getDateRetour()).isEqualTo(LocalDate.of(2025, 12, 31));
        assertThat(entity.getEtat()).isEqualTo("en_attente");
        assertThat(entity.getGainExperience()).isEqualTo(0);
    }

    @Test
    @DisplayName("toEntity - Doit gérer les champs optionnels null")
    void toEntity_ShouldHandleNullOptionalFields() {
        ParticipationEquipeInputDTO inputDTO = new ParticipationEquipeInputDTO();
        inputDTO.setEquipeId(1L);
        inputDTO.setAventurierId(2L);
        inputDTO.setDateAffectation(LocalDate.of(2025, 12, 1));
        inputDTO.setDateRetour(null);
        inputDTO.setEtat(null);
        inputDTO.setGainExperience(null);

        ParticipationEquipe entity = mapper.toEntity(inputDTO, equipe, aventurier);

        assertThat(entity).isNotNull();
        assertThat(entity.getDateAffectation()).isEqualTo(LocalDate.of(2025, 12, 1));
        assertThat(entity.getDateRetour()).isNull();
        assertThat(entity.getEtat()).isNull();
        assertThat(entity.getGainExperience()).isNull();
    }

    @Test
    @DisplayName("toEntity - Doit créer une participation minimale (champs obligatoires seulement)")
    void toEntity_ShouldCreateMinimalParticipation() {
        ParticipationEquipeInputDTO inputDTO = new ParticipationEquipeInputDTO();
        inputDTO.setEquipeId(1L);
        inputDTO.setAventurierId(2L);
        inputDTO.setDateAffectation(LocalDate.of(2025, 12, 1));

        ParticipationEquipe entity = mapper.toEntity(inputDTO, equipe, aventurier);

        assertThat(entity).isNotNull();
        assertThat(entity.getEquipe()).isNotNull();
        assertThat(entity.getAventurier()).isNotNull();
        assertThat(entity.getDateAffectation()).isNotNull();
    }

    @Test
    @DisplayName("toEntity - Doit associer correctement l'équipe et l'aventurier")
    void toEntity_ShouldCorrectlyAssociateEquipeAndAventurier() {
        ParticipationEquipeInputDTO inputDTO = new ParticipationEquipeInputDTO();
        inputDTO.setEquipeId(1L);
        inputDTO.setAventurierId(2L);
        inputDTO.setDateAffectation(LocalDate.of(2025, 12, 1));

        ParticipationEquipe entity = mapper.toEntity(inputDTO, equipe, aventurier);

        assertThat(entity.getEquipe().getId()).isEqualTo(1L);
        assertThat(entity.getEquipe().getNom()).isEqualTo("Les Braves");
        assertThat(entity.getAventurier().getId()).isEqualTo(2L);
        assertThat(entity.getAventurier().getNom()).isEqualTo("Gandalf");
    }

    @Test
    @DisplayName("toEntity - Doit créer une participation avec état en_cours")
    void toEntity_ShouldCreateParticipationWithEnCoursState() {
        ParticipationEquipeInputDTO inputDTO = new ParticipationEquipeInputDTO();
        inputDTO.setEquipeId(1L);
        inputDTO.setAventurierId(2L);
        inputDTO.setDateAffectation(LocalDate.of(2025, 12, 1));
        inputDTO.setEtat("en_cours");
        inputDTO.setGainExperience(50);

        ParticipationEquipe entity = mapper.toEntity(inputDTO, equipe, aventurier);

        assertThat(entity).isNotNull();
        assertThat(entity.getEtat()).isEqualTo("en_cours");
        assertThat(entity.getGainExperience()).isEqualTo(50);
    }

    @Test
    @DisplayName("toEntity - Doit gérer des dates identiques pour dateAffectation et dateRetour")
    void toEntity_ShouldHandleSameDatesForAffectationAndRetour() {
        LocalDate sameDate = LocalDate.of(2025, 12, 25);

        ParticipationEquipeInputDTO inputDTO = new ParticipationEquipeInputDTO();
        inputDTO.setEquipeId(1L);
        inputDTO.setAventurierId(2L);
        inputDTO.setDateAffectation(sameDate);
        inputDTO.setDateRetour(sameDate);

        ParticipationEquipe entity = mapper.toEntity(inputDTO, equipe, aventurier);

        assertThat(entity).isNotNull();
        assertThat(entity.getDateAffectation()).isEqualTo(sameDate);
        assertThat(entity.getDateRetour()).isEqualTo(sameDate);
    }
}
