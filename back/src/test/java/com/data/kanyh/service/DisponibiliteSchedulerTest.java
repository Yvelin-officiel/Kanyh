package com.data.kanyh.service;

import com.data.kanyh.model.*;
import com.data.kanyh.repository.EquipeRepository;
import com.data.kanyh.repository.ParticipationEquipeRepository;
import com.data.kanyh.repository.QueteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisponibiliteSchedulerTest {

    @Mock
    private ReposSamService reposSamService;

    @Mock
    private ParticipationEquipeRepository participationEquipeRepository;

    @Mock
    private QueteRepository queteRepository;

    @Mock
    private EquipeRepository equipeRepository;

    @InjectMocks
    private DisponibiliteScheduler disponibiliteScheduler;

    private Aventurier aventurier;
    private Equipe equipe;
    private Quete quete;
    private ParticipationEquipe participation;
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

        equipe = new Equipe();
        equipe.setId(1L);
        equipe.setNom("Équipe Test");

        participation = new ParticipationEquipe();
        participation.setId(1L);
        participation.setAventurier(aventurier);
        participation.setEquipe(equipe);
        participation.setDateAffectation(LocalDate.of(2026, 1, 1));
        participation.setDateRetour(LocalDate.of(2026, 1, 10));
        participation.setEtat("ACTIVE");

        equipe.setParticipations(Arrays.asList(participation));

        quete = new Quete();
        quete.setId(1L);
        quete.setNom("Quête Test");
        quete.setStatut(StatutQuete.TERMINEE);
        quete.setEquipeId(1L);
        quete.setExperienceRecommandee(150);
        quete.setExperienceGagnee(50);
    }

    @Test
    void testMettreAJourDisponibilites_CasNominal() {
        // Given
        when(reposSamService.mettreAJourDisponibilites()).thenReturn(2);
        when(queteRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        disponibiliteScheduler.mettreAJourDisponibilites();

        // Then
        verify(reposSamService, times(1)).mettreAJourDisponibilites();
        verify(queteRepository, times(1)).findAll();
    }

    @Test
    void testMettreAJourDisponibilites_AucuneQueteTerminee() {
        // Given
        when(reposSamService.mettreAJourDisponibilites()).thenReturn(0);
        when(queteRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        disponibiliteScheduler.mettreAJourDisponibilites();

        // Then
        verify(reposSamService).mettreAJourDisponibilites();
        verify(queteRepository).findAll();
        verify(equipeRepository, never()).findById(any());
    }

    @Test
    void testMettreAJourDisponibilites_QueteTermineeSansEquipe() {
        // Given
        quete.setEquipeId(null);
        when(reposSamService.mettreAJourDisponibilites()).thenReturn(0);
        when(queteRepository.findAll()).thenReturn(Arrays.asList(quete));

        // When
        disponibiliteScheduler.mettreAJourDisponibilites();

        // Then
        verify(reposSamService).mettreAJourDisponibilites();
        verify(queteRepository).findAll();
        verify(equipeRepository, never()).findById(any());
    }

    @Test
    void testMettreAJourDisponibilites_QueteTermineeAvecEquipe() {
        // Given
        when(reposSamService.mettreAJourDisponibilites()).thenReturn(0);
        when(queteRepository.findAll()).thenReturn(Arrays.asList(quete));
        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));
        doNothing().when(reposSamService).appliquerRepos(anyLong(), any(), any());

        // When
        disponibiliteScheduler.mettreAJourDisponibilites();

        // Then
        verify(reposSamService).mettreAJourDisponibilites();
        verify(queteRepository).findAll();
        verify(equipeRepository).findById(1L);
        verify(reposSamService).appliquerRepos(eq(1L), any(ParticipationEquipe.class), eq(quete));
    }

    @Test
    void testMettreAJourDisponibilites_AventurierDejaEnRepos() {
        // Given
        aventurier.setDisponibilite("EN_REPOS");
        when(reposSamService.mettreAJourDisponibilites()).thenReturn(0);
        when(queteRepository.findAll()).thenReturn(Arrays.asList(quete));
        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));

        // When
        disponibiliteScheduler.mettreAJourDisponibilites();

        // Then
        verify(reposSamService).mettreAJourDisponibilites();
        verify(reposSamService, never()).appliquerRepos(anyLong(), any(), any());
    }

    @Test
    void testMettreAJourDisponibilites_ParticipationSansDateRetour() {
        // Given
        participation.setDateRetour(null);
        when(reposSamService.mettreAJourDisponibilites()).thenReturn(0);
        when(queteRepository.findAll()).thenReturn(Arrays.asList(quete));
        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));
        doNothing().when(reposSamService).appliquerRepos(anyLong(), any(), any());

        // When
        disponibiliteScheduler.mettreAJourDisponibilites();

        // Then
        verify(reposSamService).mettreAJourDisponibilites();
        verify(equipeRepository).findById(1L);
        // La dateRetour devrait être mise à jour automatiquement
        verify(reposSamService).appliquerRepos(eq(1L), any(ParticipationEquipe.class), eq(quete));
    }

    @Test
    void testMettreAJourDisponibilites_PlusieursQuetesTerminees() {
        // Given
        Quete quete2 = new Quete();
        quete2.setId(2L);
        quete2.setNom("Quête Test 2");
        quete2.setStatut(StatutQuete.TERMINEE);
        quete2.setEquipeId(2L);
        quete2.setExperienceRecommandee(100);

        Equipe equipe2 = new Equipe();
        equipe2.setId(2L);
        equipe2.setNom("Équipe Test 2");

        Aventurier aventurier2 = new Aventurier();
        aventurier2.setId(2L);
        aventurier2.setDisponibilite("EN_MISSION");

        ParticipationEquipe participation2 = new ParticipationEquipe();
        participation2.setId(2L);
        participation2.setAventurier(aventurier2);
        participation2.setEquipe(equipe2);
        participation2.setDateRetour(LocalDate.now());

        equipe2.setParticipations(Arrays.asList(participation2));

        when(reposSamService.mettreAJourDisponibilites()).thenReturn(0);
        when(queteRepository.findAll()).thenReturn(Arrays.asList(quete, quete2));
        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));
        when(equipeRepository.findById(2L)).thenReturn(Optional.of(equipe2));
        doNothing().when(reposSamService).appliquerRepos(anyLong(), any(), any());

        // When
        disponibiliteScheduler.mettreAJourDisponibilites();

        // Then
        verify(reposSamService).mettreAJourDisponibilites();
        verify(equipeRepository).findById(1L);
        verify(equipeRepository).findById(2L);
        verify(reposSamService, times(2)).appliquerRepos(anyLong(), any(), any());
    }

    @Test
    void testMettreAJourDisponibilites_QueteEnCoursIgnoree() {
        // Given
        quete.setStatut(StatutQuete.EN_COURS);
        when(reposSamService.mettreAJourDisponibilites()).thenReturn(0);
        when(queteRepository.findAll()).thenReturn(Arrays.asList(quete));

        // When
        disponibiliteScheduler.mettreAJourDisponibilites();

        // Then
        verify(reposSamService).mettreAJourDisponibilites();
        verify(equipeRepository, never()).findById(any());
        verify(reposSamService, never()).appliquerRepos(anyLong(), any(), any());
    }

    @Test
    void testMettreAJourDisponibilites_QueteNouvelleIgnoree() {
        // Given
        quete.setStatut(StatutQuete.NOUVELLE);
        when(reposSamService.mettreAJourDisponibilites()).thenReturn(0);
        when(queteRepository.findAll()).thenReturn(Arrays.asList(quete));

        // When
        disponibiliteScheduler.mettreAJourDisponibilites();

        // Then
        verify(reposSamService).mettreAJourDisponibilites();
        verify(equipeRepository, never()).findById(any());
        verify(reposSamService, never()).appliquerRepos(anyLong(), any(), any());
    }

    @Test
    void testMettreAJourDisponibilites_EquipeNonTrouvee() {
        // Given
        when(reposSamService.mettreAJourDisponibilites()).thenReturn(0);
        when(queteRepository.findAll()).thenReturn(Arrays.asList(quete));
        when(equipeRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        disponibiliteScheduler.mettreAJourDisponibilites();

        // Then
        verify(reposSamService).mettreAJourDisponibilites();
        verify(equipeRepository).findById(1L);
        verify(reposSamService, never()).appliquerRepos(anyLong(), any(), any());
    }

    @Test
    void testMettreAJourDisponibilites_GestionErreur() {
        // Given
        when(reposSamService.mettreAJourDisponibilites()).thenReturn(0);
        when(queteRepository.findAll()).thenReturn(Arrays.asList(quete));
        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));
        doThrow(new RuntimeException("Erreur de test"))
                .when(reposSamService).appliquerRepos(anyLong(), any(), any());

        // When - ne devrait pas lancer d'exception
        disponibiliteScheduler.mettreAJourDisponibilites();

        // Then - l'erreur est loggée mais ne bloque pas
        verify(reposSamService).mettreAJourDisponibilites();
        verify(equipeRepository).findById(1L);
        verify(reposSamService).appliquerRepos(eq(1L), any(ParticipationEquipe.class), eq(quete));
    }

    @Test
    void testMettreAJourDisponibilites_EquipeAvecPlusieursAventuriers() {
        // Given
        Aventurier aventurier2 = new Aventurier();
        aventurier2.setId(2L);
        aventurier2.setDisponibilite("EN_MISSION");

        ParticipationEquipe participation2 = new ParticipationEquipe();
        participation2.setId(2L);
        participation2.setAventurier(aventurier2);
        participation2.setDateRetour(LocalDate.now());
        // Note: ne pas définir equipe sur participation2 pour éviter référence circulaire

        // Créer une nouvelle liste de participations sans références circulaires
        List<ParticipationEquipe> participations = Arrays.asList(participation, participation2);

        // Créer une équipe sans références circulaires
        Equipe equipeTest = new Equipe();
        equipeTest.setId(1L);
        equipeTest.setNom("Équipe Test");
        equipeTest.setParticipations(participations);

        when(reposSamService.mettreAJourDisponibilites()).thenReturn(0);
        when(queteRepository.findAll()).thenReturn(Arrays.asList(quete));
        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipeTest));
        doNothing().when(reposSamService).appliquerRepos(anyLong(), any(), any());

        // When
        disponibiliteScheduler.mettreAJourDisponibilites();

        // Then
        verify(reposSamService).mettreAJourDisponibilites();
        verify(equipeRepository).findById(1L);
        verify(reposSamService, times(2)).appliquerRepos(anyLong(), any(), any());
    }

    @Test
    void testMettreAJourDisponibilites_AventurierDisponibleIgnore() {
        // Given
        aventurier.setDisponibilite("DISPONIBLE");

        // Créer une nouvelle participation sans référence circulaire pour éviter StackOverflowError
        ParticipationEquipe participationTest = new ParticipationEquipe();
        participationTest.setId(1L);
        participationTest.setAventurier(aventurier);
        participationTest.setDateAffectation(LocalDate.of(2026, 1, 1));
        participationTest.setDateRetour(LocalDate.of(2026, 1, 10));
        participationTest.setEtat("ACTIVE");
        // Note: ne pas définir equipe sur participation pour éviter référence circulaire

        // Créer une équipe sans référence circulaire
        Equipe equipeTest = new Equipe();
        equipeTest.setId(1L);
        equipeTest.setNom("Équipe Test");
        equipeTest.setParticipations(Arrays.asList(participationTest));

        when(reposSamService.mettreAJourDisponibilites()).thenReturn(0);
        when(queteRepository.findAll()).thenReturn(Arrays.asList(quete));
        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipeTest));

        // When
        disponibiliteScheduler.mettreAJourDisponibilites();

        // Then
        verify(reposSamService).mettreAJourDisponibilites();
        verify(equipeRepository).findById(1L);
        verify(reposSamService, never()).appliquerRepos(anyLong(), any(), any());
    }

    @Test
    void testMettreAJourDisponibilites_EquipeSansParticipations() {
        // Given
        equipe.setParticipations(Collections.emptyList());
        when(reposSamService.mettreAJourDisponibilites()).thenReturn(0);
        when(queteRepository.findAll()).thenReturn(Arrays.asList(quete));
        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));

        // When
        disponibiliteScheduler.mettreAJourDisponibilites();

        // Then
        verify(reposSamService).mettreAJourDisponibilites();
        verify(equipeRepository).findById(1L);
        verify(reposSamService, never()).appliquerRepos(anyLong(), any(), any());
    }

    @Test
    void testMettreAJourDisponibilites_ErreurGlobaleGereeSansBlocage() {
        // Given
        when(reposSamService.mettreAJourDisponibilites()).thenThrow(new RuntimeException("Erreur critique"));

        // When - ne devrait pas lancer d'exception
        disponibiliteScheduler.mettreAJourDisponibilites();

        // Then - l'erreur est loggée mais le scheduler continue
        verify(reposSamService).mettreAJourDisponibilites();
        // Pas d'appel aux autres services car l'erreur survient tôt
    }
}
