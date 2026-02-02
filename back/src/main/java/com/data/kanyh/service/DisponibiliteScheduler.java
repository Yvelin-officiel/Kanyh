package com.data.kanyh.service;

import com.data.kanyh.model.ParticipationEquipe;
import com.data.kanyh.model.Quete;
import com.data.kanyh.model.StatutQuete;
import com.data.kanyh.repository.EquipeRepository;
import com.data.kanyh.repository.ParticipationEquipeRepository;
import com.data.kanyh.repository.QueteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Scheduler pour la gestion automatique des disponibilités des aventuriers.
 * S'exécute toutes les heures pour :
 * - Mettre à jour les aventuriers EN_REPOS → DISPONIBLE
 * - Traiter les aventuriers en mission dont la quête est terminée
 */
@Component
@AllArgsConstructor
@Slf4j
public class DisponibiliteScheduler {

    private final ReposSamService reposSamService;
    private final ParticipationEquipeRepository participationEquipeRepository;
    private final QueteRepository queteRepository;
    private final EquipeRepository equipeRepository;

    /**
     * Tâche planifiée qui s'exécute toutes les heures (à l'heure pile).
     * Cron expression : "0 0 * * * *" = à 0 minute, 0 seconde, toutes les heures.
     */
    @Scheduled(cron = "0 0 * * * *")
    public void mettreAJourDisponibilites() {
        log.info("Début de la mise à jour automatique des disponibilités");

        try {
            // Tâche 1 : Mettre à jour les aventuriers EN_REPOS → DISPONIBLE
            int aventuriersLiberes = reposSamService.mettreAJourDisponibilites();
            log.info("{} aventurier(s) passé(s) de EN_REPOS à DISPONIBLE", aventuriersLiberes);

            // Tâche 2 : Traiter les aventuriers en mission dont la quête est terminée
            int aventuriersEnRepos = traiterQuetesTerminees();
            log.info("{} aventurier(s) mis en repos suite à une quête terminée", aventuriersEnRepos);

            log.info("Fin de la mise à jour automatique des disponibilités");
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour des disponibilités : {}", e.getMessage(), e);
        }
    }

    /**
     * Traite les aventuriers en mission dont la quête est terminée mais qui n'ont pas encore
     * été mis en repos (cas de rattrapage).
     *
     * @return le nombre d'aventuriers mis en repos
     */
    private int traiterQuetesTerminees() {
        AtomicInteger count = new AtomicInteger(0);

        // Récupère toutes les quêtes terminées
        List<Quete> quetesTerminees = queteRepository.findAll().stream()
                .filter(q -> q.getStatut() == StatutQuete.TERMINEE)
                .filter(q -> q.getEquipeId() != null)
                .toList();

        for (Quete quete : quetesTerminees) {
            equipeRepository.findById(quete.getEquipeId()).ifPresent(equipe -> {
                List<ParticipationEquipe> participations = equipe.getParticipations();

                for (ParticipationEquipe participation : participations) {
                    // Vérifie si l'aventurier est encore EN_MISSION et devrait être mis en repos
                    String disponibilite = participation.getAventurier().getDisponibilite();

                    // Ne traiter que les aventuriers EN_MISSION (pas les DISPONIBLES ni ceux déjà EN_REPOS)
                    if ("EN_MISSION".equals(disponibilite)) {
                        try {
                            // Mettre à jour dateRetour si non définie
                            if (participation.getDateRetour() == null) {
                                participation.setDateRetour(LocalDate.now());
                            }

                            // Vérifier que la mission est réellement terminée (dateRetour présente)
                            if (participation.getDateRetour() != null) {
                                reposSamService.appliquerRepos(
                                        participation.getAventurier().getId(),
                                        participation,
                                        quete
                                );
                                count.incrementAndGet();

                                log.info("Repos SAM appliqué (rattrapage) à l'aventurier {} pour la quête {}",
                                         participation.getAventurier().getId(), quete.getId());
                            }
                        } catch (Exception e) {
                            log.error("Erreur lors de l'application du repos (rattrapage) pour l'aventurier {} : {}",
                                     participation.getAventurier().getId(), e.getMessage());
                        }
                    }
                }
            });
        }

        return count.get();
    }
}
