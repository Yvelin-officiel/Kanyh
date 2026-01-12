package com.data.kanyh.service;

import com.data.kanyh.dto.TeamSuggestionDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.model.Quete;
import com.data.kanyh.model.Specialite;
import com.data.kanyh.repository.AventurierRepository;
import com.data.kanyh.repository.QueteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamGenerationServiceTest {

    @Mock
    private QueteRepository queteRepository;

    @Mock
    private AventurierRepository aventurierRepository;

    @InjectMocks
    private TeamGenerationService teamGenerationService;

    private Quete quete;
    private Specialite specialiteEpee;
    private Specialite specialiteMagie;

    @BeforeEach
    void setUp() {
        specialiteEpee = new Specialite(1, "Epée");
        specialiteMagie = new Specialite(2, "Magie");

        quete = new Quete();
        quete.setId(1L);
        quete.setNom("Quête Test");
        quete.setExperienceGagnee(100);
        quete.setSpecialitesRequises(Collections.singletonList(specialiteEpee));
        quete.setDatePeremption(LocalDate.now().plusDays(10));
        quete.setDureeEstimee(5);
    }

    @Test
    void testGenerateTeam_Success() {
        // Aventurier parfait: bonne spé, bon niveau (ratio 1.0), dispo, cout 50
        Aventurier a1 = createAventurier(1L, "A1", 100, specialiteEpee, "DISPONIBLE", 50.0);

        // Aventurier moyen: bonne spé, niveau limite (50 vs 100 -> ratio 0.5 -> score XP moindre), dispo, cout 50
        // Il doit être accepté car niveau 50 est le min (0.5 * 100)
        Aventurier a2 = createAventurier(2L, "A2", 50, specialiteEpee, "DISPONIBLE", 50.0);

        // Aventurier niveau trop faible (exclu car 40 < 50)
        Aventurier a3 = createAventurier(3L, "A3", 40, specialiteEpee, "DISPONIBLE", 20.0);

        // Aventurier indisponible (exclu par date)
        Aventurier a4 = createAventurier(4L, "A4", 100, specialiteEpee, "OCCUPE", 50.0);
        a4.setDateDisponibilite(LocalDate.now().plusDays(20)); // Après date péremption quête

        when(queteRepository.findById(1L)).thenReturn(Optional.of(quete));
        when(aventurierRepository.findAll()).thenReturn(Arrays.asList(a1, a2, a3, a4));

        TeamSuggestionDTO result = teamGenerationService.generateTeam(1L, 4);

        assertNotNull(result);
        assertEquals(2, result.getAventurierIds().size(), "Seuls A1 et A2 devraient être sélectionnés (A3 trop faible, A4 indispo)");
        assertTrue(result.getAventurierIds().contains(1L));
        assertTrue(result.getAventurierIds().contains(2L));

        assertEquals(1L, result.getAventurierIds().get(0), "Le meilleur candidat (A1) doit être premier");
    }

    @Test
    void testGenerateTeam_QueteNotFound() {
        when(queteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> teamGenerationService.generateTeam(1L, 4));
    }

    @Test
    void testGenerateTeam_RespectLimit() {
        Aventurier a1 = createAventurier(1L, "A1", 100, specialiteEpee, "DISPONIBLE", 50.0);
        Aventurier a2 = createAventurier(2L, "A2", 100, specialiteEpee, "DISPONIBLE", 50.0);
        Aventurier a3 = createAventurier(3L, "A3", 100, specialiteEpee, "DISPONIBLE", 50.0);

        when(queteRepository.findById(1L)).thenReturn(Optional.of(quete));
        when(aventurierRepository.findAll()).thenReturn(Arrays.asList(a1, a2, a3));

        TeamSuggestionDTO result = teamGenerationService.generateTeam(1L, 2);

        assertEquals(2, result.getAventurierIds().size());
    }

    private Aventurier createAventurier(Long id, String nom, int niveau, Specialite spe, String dispo, Double taux) {
        Aventurier a = new Aventurier();
        a.setId(id);
        a.setNom(nom);
        a.setNiveauExperience(niveau);
        a.setSpecialite(spe);
        a.setDisponibilite(dispo);
        a.setTauxJournalierBase(taux);
        return a;
    }
}

