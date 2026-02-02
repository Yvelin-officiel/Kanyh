package com.data.kanyh.service;

import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.model.ParticipationEquipe;
import com.data.kanyh.model.Quete;
import com.data.kanyh.model.Specialite;
import com.data.kanyh.repository.AventurierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReposSamServiceTest {

    @Mock
    private AventurierRepository aventurierRepository;

    @InjectMocks
    private ReposSamService reposSamService;

    private Aventurier aventurier;
    private ParticipationEquipe participation;
    private Quete quete;
    private Specialite specialite;

    @BeforeEach
    void setUp() {
        specialite = new Specialite();
        specialite.setId(1);
        specialite.setNom("Guerrier");

        aventurier = new Aventurier();
        aventurier.setId(1L);
        aventurier.setNom("Test Aventurier");
        aventurier.setNiveauExperience(100);
        aventurier.setSpecialite(specialite);
        aventurier.setDisponibilite("EN_MISSION");

        participation = new ParticipationEquipe();
        participation.setId(1L);
        participation.setAventurier(aventurier);
        participation.setDateAffectation(LocalDate.of(2026, 1, 1));
        participation.setDateRetour(LocalDate.of(2026, 1, 11)); // 10 jours de mission

        quete = new Quete();
        quete.setId(1L);
        quete.setNom("Quête Test");
        quete.setExperienceRecommandee(150);
        quete.setExperienceGagnee(50);
    }

    @Test
    void testCalculerDureeRepos_CasNominal() {
        // Given: Exp_r = 100, Exp_rec = 150, D_q = 10 jours
        // D_r = 0.5 × (150 / 100) × 10 = 0.5 × 1.5 × 10 = 7.5 → arrondi à 8 jours

        // When
        int dureeRepos = reposSamService.calculerDureeRepos(aventurier, participation, quete);

        // Then
        assertEquals(8, dureeRepos, "La durée de repos devrait être arrondie à 8 jours");
    }

    @Test
    void testCalculerDureeRepos_ArrondiSuperieur_PetiteValeur() {
        // Given: Exp_r = 100, Exp_rec = 10, D_q = 1 jour
        // D_r = 0.5 × (10 / 100) × 1 = 0.5 × 0.1 × 1 = 0.05 → arrondi à 1 jour
        quete.setExperienceRecommandee(10);
        participation.setDateRetour(LocalDate.of(2026, 1, 2)); // 1 jour de mission

        // When
        int dureeRepos = reposSamService.calculerDureeRepos(aventurier, participation, quete);

        // Then
        assertEquals(1, dureeRepos, "La durée de repos minimale devrait être 1 jour");
    }

    @Test
    void testCalculerDureeRepos_ArrondiSuperieur_0Point3Jour() {
        // Given: Exp_r = 100, Exp_rec = 20, D_q = 3 jours
        // D_r = 0.5 × (20 / 100) × 3 = 0.5 × 0.2 × 3 = 0.3 → arrondi à 1 jour
        quete.setExperienceRecommandee(20);
        participation.setDateRetour(LocalDate.of(2026, 1, 4)); // 3 jours de mission

        // When
        int dureeRepos = reposSamService.calculerDureeRepos(aventurier, participation, quete);

        // Then
        assertEquals(1, dureeRepos, "0.3 jour devrait être arrondi à 1 jour");
    }

    @Test
    void testCalculerDureeRepos_ArrondiSuperieur_2Point8Jours() {
        // Given: Exp_r = 100, Exp_rec = 140, D_q = 4 jours
        // D_r = 0.5 × (140 / 100) × 4 = 0.5 × 1.4 × 4 = 2.8 → arrondi à 3 jours
        quete.setExperienceRecommandee(140);
        participation.setDateRetour(LocalDate.of(2026, 1, 5)); // 4 jours de mission

        // When
        int dureeRepos = reposSamService.calculerDureeRepos(aventurier, participation, quete);

        // Then
        assertEquals(3, dureeRepos, "2.8 jours devrait être arrondi à 3 jours");
    }

    @Test
    void testCalculerDureeRepos_ExperienceRecommandeeNull_UtiliseExperienceGagnee() {
        // Given: Exp_rec = null, utilise experienceGagnee = 50
        // D_r = 0.5 × (50 / 100) × 10 = 0.5 × 0.5 × 10 = 2.5 → arrondi à 3 jours
        quete.setExperienceRecommandee(null);

        // When
        int dureeRepos = reposSamService.calculerDureeRepos(aventurier, participation, quete);

        // Then
        assertEquals(3, dureeRepos, "Devrait utiliser experienceGagnee quand experienceRecommandee est null");
    }

    @Test
    void testCalculerDureeRepos_ExperienceRecommandeeZero_UtiliseExperienceGagnee() {
        // Given: Exp_rec = 0, utilise experienceGagnee = 50
        quete.setExperienceRecommandee(0);

        // When
        int dureeRepos = reposSamService.calculerDureeRepos(aventurier, participation, quete);

        // Then
        assertEquals(3, dureeRepos, "Devrait utiliser experienceGagnee quand experienceRecommandee est 0");
    }

    @Test
    void testCalculerDureeRepos_ExperienceAventurierZero_RetourneMinimum() {
        // Given: Exp_r = 0 (cas limite, division par zéro évitée)
        aventurier.setNiveauExperience(0);

        // When
        int dureeRepos = reposSamService.calculerDureeRepos(aventurier, participation, quete);

        // Then
        assertEquals(1, dureeRepos, "Devrait retourner 1 jour minimum quand l'expérience de l'aventurier est 0");
    }

    @Test
    void testCalculerDureeRepos_DatesInvalides_RetourneMinimum() {
        // Given: dates de participation invalides
        participation.setDateAffectation(null);

        // When
        int dureeRepos = reposSamService.calculerDureeRepos(aventurier, participation, quete);

        // Then
        assertEquals(1, dureeRepos, "Devrait retourner 1 jour minimum quand les dates sont invalides");
    }

    @Test
    void testCalculerDureeRepos_DateRetourNull_RetourneMinimum() {
        // Given: date de retour null
        participation.setDateRetour(null);

        // When
        int dureeRepos = reposSamService.calculerDureeRepos(aventurier, participation, quete);

        // Then
        assertEquals(1, dureeRepos, "Devrait retourner 1 jour minimum quand dateRetour est null");
    }

    @Test
    void testCalculerDureeRepos_MissionSuperieureAExperience() {
        // Given: Exp_rec > Exp_r (mission difficile)
        // Exp_r = 50, Exp_rec = 200, D_q = 10 jours
        // D_r = 0.5 × (200 / 50) × 10 = 0.5 × 4 × 10 = 20 jours
        aventurier.setNiveauExperience(50);
        quete.setExperienceRecommandee(200);

        // When
        int dureeRepos = reposSamService.calculerDureeRepos(aventurier, participation, quete);

        // Then
        assertEquals(20, dureeRepos, "Mission difficile devrait donner un repos plus long");
    }

    @Test
    void testAppliquerRepos_CasNominal() {
        // Given
        when(aventurierRepository.findById(1L)).thenReturn(Optional.of(aventurier));
        when(aventurierRepository.save(any(Aventurier.class))).thenReturn(aventurier);

        // When
        reposSamService.appliquerRepos(1L, participation, quete);

        // Then
        ArgumentCaptor<Aventurier> captor = ArgumentCaptor.forClass(Aventurier.class);
        verify(aventurierRepository).save(captor.capture());

        Aventurier savedAventurier = captor.getValue();
        assertEquals("EN_REPOS", savedAventurier.getDisponibilite());
        assertNotNull(savedAventurier.getDateDisponibilite());

        // Date de disponibilité = dateRetour + durée repos (8 jours dans ce cas)
        LocalDate expectedDate = LocalDate.of(2026, 1, 19); // 11 janvier + 8 jours
        assertEquals(expectedDate, savedAventurier.getDateDisponibilite());
    }

    @Test
    void testAppliquerRepos_AventurierNonTrouve() {
        // Given
        when(aventurierRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () ->
            reposSamService.appliquerRepos(999L, participation, quete)
        );
    }

    @Test
    void testAppliquerRepos_DateRetourNull_UtiliseDateActuelle() {
        // Given
        participation.setDateRetour(null);
        when(aventurierRepository.findById(1L)).thenReturn(Optional.of(aventurier));
        when(aventurierRepository.save(any(Aventurier.class))).thenReturn(aventurier);

        // When
        reposSamService.appliquerRepos(1L, participation, quete);

        // Then
        ArgumentCaptor<Aventurier> captor = ArgumentCaptor.forClass(Aventurier.class);
        verify(aventurierRepository).save(captor.capture());

        Aventurier savedAventurier = captor.getValue();
        assertEquals("EN_REPOS", savedAventurier.getDisponibilite());
        assertNotNull(savedAventurier.getDateDisponibilite());
        assertTrue(savedAventurier.getDateDisponibilite().isAfter(LocalDate.now()) ||
                   savedAventurier.getDateDisponibilite().isEqual(LocalDate.now().plusDays(1)));
    }

    @Test
    void testMettreAJourDisponibilites_AucunAventurierEnRepos() {
        // Given
        when(aventurierRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        int result = reposSamService.mettreAJourDisponibilites();

        // Then
        assertEquals(0, result);
        verify(aventurierRepository, never()).save(any());
    }

    @Test
    void testMettreAJourDisponibilites_AventurierDateNonAtteinte() {
        // Given: aventurier en repos mais date future
        aventurier.setDisponibilite("EN_REPOS");
        aventurier.setDateDisponibilite(LocalDate.now().plusDays(5));
        when(aventurierRepository.findAll()).thenReturn(Arrays.asList(aventurier));

        // When
        int result = reposSamService.mettreAJourDisponibilites();

        // Then
        assertEquals(0, result, "Ne devrait pas mettre à jour un aventurier dont la date n'est pas atteinte");
        verify(aventurierRepository, never()).save(any());
    }

    @Test
    void testMettreAJourDisponibilites_AventurierDateAtteinte() {
        // Given: aventurier en repos avec date atteinte
        aventurier.setDisponibilite("EN_REPOS");
        aventurier.setDateDisponibilite(LocalDate.now().minusDays(1));
        when(aventurierRepository.findAll()).thenReturn(Arrays.asList(aventurier));
        when(aventurierRepository.save(any(Aventurier.class))).thenReturn(aventurier);

        // When
        int result = reposSamService.mettreAJourDisponibilites();

        // Then
        assertEquals(1, result, "Devrait mettre à jour 1 aventurier");
        ArgumentCaptor<Aventurier> captor = ArgumentCaptor.forClass(Aventurier.class);
        verify(aventurierRepository).save(captor.capture());
        assertEquals("DISPONIBLE", captor.getValue().getDisponibilite());
    }

    @Test
    void testMettreAJourDisponibilites_AventurierDateExacte() {
        // Given: aventurier en repos avec date exacte aujourd'hui
        aventurier.setDisponibilite("EN_REPOS");
        aventurier.setDateDisponibilite(LocalDate.now());
        when(aventurierRepository.findAll()).thenReturn(Arrays.asList(aventurier));
        when(aventurierRepository.save(any(Aventurier.class))).thenReturn(aventurier);

        // When
        int result = reposSamService.mettreAJourDisponibilites();

        // Then
        assertEquals(1, result, "Devrait mettre à jour un aventurier dont la date est aujourd'hui");
        verify(aventurierRepository).save(any());
    }

    @Test
    void testMettreAJourDisponibilites_PlusieursAventuriers() {
        // Given: plusieurs aventuriers en repos
        Aventurier aventurier2 = new Aventurier();
        aventurier2.setId(2L);
        aventurier2.setDisponibilite("EN_REPOS");
        aventurier2.setDateDisponibilite(LocalDate.now().minusDays(1));

        aventurier.setDisponibilite("EN_REPOS");
        aventurier.setDateDisponibilite(LocalDate.now().minusDays(2));

        when(aventurierRepository.findAll()).thenReturn(Arrays.asList(aventurier, aventurier2));
        when(aventurierRepository.save(any(Aventurier.class))).thenReturn(aventurier);

        // When
        int result = reposSamService.mettreAJourDisponibilites();

        // Then
        assertEquals(2, result, "Devrait mettre à jour 2 aventuriers");
        verify(aventurierRepository, times(2)).save(any());
    }

    @Test
    void testMettreAJourDisponibilites_AventurierDateDisponibiliteNull() {
        // Given: aventurier en repos mais sans date de disponibilité
        aventurier.setDisponibilite("EN_REPOS");
        aventurier.setDateDisponibilite(null);
        when(aventurierRepository.findAll()).thenReturn(Arrays.asList(aventurier));

        // When
        int result = reposSamService.mettreAJourDisponibilites();

        // Then
        assertEquals(0, result, "Ne devrait pas traiter un aventurier sans dateDisponibilite");
        verify(aventurierRepository, never()).save(any());
    }

    @Test
    void testMettreAJourDisponibilites_IgnoreAventuriersDisponibles() {
        // Given: aventurier déjà disponible
        aventurier.setDisponibilite("DISPONIBLE");
        aventurier.setDateDisponibilite(LocalDate.now().minusDays(1));
        when(aventurierRepository.findAll()).thenReturn(Arrays.asList(aventurier));

        // When
        int result = reposSamService.mettreAJourDisponibilites();

        // Then
        assertEquals(0, result, "Ne devrait pas traiter les aventuriers déjà DISPONIBLE");
        verify(aventurierRepository, never()).save(any());
    }
}
