package com.data.kanyh.service;

import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.model.ParticipationEquipe;
import com.data.kanyh.model.Quete;
import com.data.kanyh.repository.AventurierRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
@Slf4j
public class ReposSamService {

    private final AventurierRepository aventurierRepository;

    /**
     * Calcule la durée de repos selon la formule SAM.
     * D_r = 0.5 × (Exp_rec / Exp_r) × D_q
     * Le résultat est arrondi au jour supérieur.
     *
     * @param aventurier l'aventurier concerné
     * @param participation la participation à la mission
     * @param quete la quête terminée
     * @return la durée de repos en jours (arrondie au supérieur)
     */
    public int calculerDureeRepos(Aventurier aventurier, ParticipationEquipe participation, Quete quete) {
        // Exp_r : expérience réelle de l'aventurier
        int expR = aventurier.getNiveauExperience();

        // Exp_rec : expérience recommandée pour la mission
        Integer expRec = quete.getExperienceRecommandee();

        // Si Exp_rec n'est pas défini, utiliser experienceGagnee comme estimation
        if (expRec == null || expRec == 0) {
            expRec = quete.getExperienceGagnee();
            if (expRec == null || expRec == 0) {
                // Valeur par défaut si aucune info disponible
                expRec = expR;
                log.warn("Experience recommandée non définie pour la quête {}. Utilisation de l'expérience de l'aventurier.", quete.getId());
            }
        }

        // D_q : durée de la mission en jours
        LocalDate dateDebut = participation.getDateAffectation();
        LocalDate dateFin = participation.getDateRetour();

        if (dateDebut == null || dateFin == null) {
            log.warn("Dates de participation invalides pour l'aventurier {} sur la quête {}", aventurier.getId(), quete.getId());
            return 1; // Repos minimum d'1 jour
        }

        long dQ = ChronoUnit.DAYS.between(dateDebut, dateFin);
        if (dQ <= 0) {
            dQ = 1; // Minimum 1 jour de mission
        }

        // Éviter division par zéro
        if (expR == 0) {
            log.warn("Expérience de l'aventurier {} est 0. Repos minimum appliqué.", aventurier.getId());
            return 1;
        }

        // Calcul de D_r selon la formule SAM
        double dR = 0.5 * ((double) expRec / expR) * dQ;

        // Arrondi au jour supérieur
        int dureeRepos = (int) Math.ceil(dR);

        // Minimum 1 jour de repos
        if (dureeRepos < 1) {
            dureeRepos = 1;
        }

        log.info("Calcul repos SAM pour aventurier {} : Exp_r={}, Exp_rec={}, D_q={}, D_r={}",
                 aventurier.getId(), expR, expRec, dQ, dureeRepos);

        return dureeRepos;
    }

    /**
     * Applique le repos à un aventurier : met à jour son statut à EN_REPOS
     * et calcule la date de disponibilité.
     *
     * @param aventurierId l'identifiant de l'aventurier
     * @param participation la participation à la mission terminée
     * @param quete la quête terminée
     */
    @Transactional
    public void appliquerRepos(Long aventurierId, ParticipationEquipe participation, Quete quete) {
        Aventurier aventurier = aventurierRepository.findById(aventurierId)
                .orElseThrow(() -> new NotFoundException("Aventurier non trouvé"));

        // Calcule la durée de repos
        int dureeRepos = calculerDureeRepos(aventurier, participation, quete);

        // Met à jour le statut
        aventurier.setDisponibilite("EN_REPOS");

        // Calcule la date de disponibilité
        LocalDate dateRetour = participation.getDateRetour();
        if (dateRetour == null) {
            dateRetour = LocalDate.now();
        }
        LocalDate dateDisponibilite = dateRetour.plusDays(dureeRepos);
        aventurier.setDateDisponibilite(dateDisponibilite);

        // Sauvegarde
        aventurierRepository.save(aventurier);

        log.info("Repos appliqué à l'aventurier {} : {} jours jusqu'au {}",
                 aventurierId, dureeRepos, dateDisponibilite);
    }

    /**
     * Met à jour les aventuriers EN_REPOS dont la date de disponibilité est atteinte
     * pour les passer à DISPONIBLE.
     *
     * @return le nombre d'aventuriers mis à jour
     */
    @Transactional
    public int mettreAJourDisponibilites() {
        LocalDate maintenant = LocalDate.now();

        // Récupère les aventuriers en repos dont la date est dépassée
        var aventuriers = aventurierRepository.findAll().stream()
                .filter(a -> "EN_REPOS".equals(a.getDisponibilite()))
                .filter(a -> a.getDateDisponibilite() != null)
                .filter(a -> !a.getDateDisponibilite().isAfter(maintenant))
                .toList();

        // Met à jour leur statut
        aventuriers.forEach(aventurier -> {
            aventurier.setDisponibilite("DISPONIBLE");
            aventurierRepository.save(aventurier);
            log.info("Aventurier {} est maintenant DISPONIBLE", aventurier.getId());
        });

        return aventuriers.size();
    }
}
