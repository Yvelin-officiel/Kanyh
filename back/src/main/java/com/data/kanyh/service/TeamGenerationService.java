package com.data.kanyh.service;

import com.data.kanyh.dto.TeamSuggestionDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.model.Quete;
import com.data.kanyh.repository.AventurierRepository;
import com.data.kanyh.repository.QueteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service de génération automatique d'équipe pour une quête.
 * Implémente un algorithme de matching basé sur la disponibilité,
 * les spécialités requises, le niveau d'expérience et le ratio coût/performance.
 */
@Service
@AllArgsConstructor
public class TeamGenerationService {

    private final QueteRepository queteRepository;
    private final AventurierRepository aventurierRepository;

    private static final String QUETE_NOT_FOUND = "Quête non trouvée";
    private static final String DISPONIBLE = "DISPONIBLE";

    /**
     * Génère une suggestion d'équipe pour une quête donnée.
     *
     * @param queteId l'identifiant de la quête
     * @param nombreParticipants le nombre d'aventuriers souhaités dans l'équipe
     * @return un {@link TeamSuggestionDTO} contenant les IDs des aventuriers suggérés
     * @throws NotFoundException si la quête n'existe pas
     */
    public TeamSuggestionDTO generateTeam(Long queteId, Integer nombreParticipants) {
        // 1. Récupérer la quête
        Quete quete = queteRepository.findById(queteId)
                .orElseThrow(() -> new NotFoundException(QUETE_NOT_FOUND));

        // 2. Récupérer tous les aventuriers
        List<Aventurier> tousLesAventuriers = aventurierRepository.findAll();

        // 3. Filtrer les aventuriers éligibles
        List<Aventurier> eligibles = filtrerAventuriersEligibles(tousLesAventuriers, quete);

        // 4. Scorer et trier
        List<Aventurier> meilleurs = scorerEtTrier(eligibles, quete);

        // 5. Sélectionner le nombre demandé
        List<Aventurier> equipeSelectionnee = meilleurs.stream()
                .limit(nombreParticipants != null ? nombreParticipants : 4)
                .toList();

        // 6. Construire le DTO de réponse
        return construireReponse(equipeSelectionnee, quete);
    }

    /**
     * Filtre les aventuriers éligibles en fonction de critères obligatoires :
     * - Disponibilité (date ou statut)
     * - Spécialité cohérente avec la quête
     * - Niveau d'expérience dans l'intervalle approprié
     *
     * @param aventuriers la liste de tous les aventuriers
     * @param quete la quête pour laquelle on cherche une équipe
     * @return la liste des aventuriers éligibles
     */
    private List<Aventurier> filtrerAventuriersEligibles(List<Aventurier> aventuriers, Quete quete) {
        return aventuriers.stream()
                .filter(aventurier -> estDisponible(aventurier, quete))
                .filter(aventurier -> aSpecialiteRequise(aventurier, quete))
                .filter(aventurier -> aNiveauExperienceAdapte(aventurier, quete))
                .toList();
    }

    /**
     * Vérifie si un aventurier est disponible pour la quête.
     *
     * @param aventurier l'aventurier à vérifier
     * @param quete la quête
     * @return true si l'aventurier est disponible
     */
    private boolean estDisponible(Aventurier aventurier, Quete quete) {
        // Vérifie si le statut est "DISPONIBLE"
        if (DISPONIBLE.equals(aventurier.getDisponibilite())) {
            return true;
        }

        // Sinon, vérifie si la date de disponibilité est avant la date de péremption de la quête
        if (aventurier.getDateDisponibilite() != null && quete.getDatePeremption() != null) {
            return aventurier.getDateDisponibilite().isBefore(quete.getDatePeremption())
                    || aventurier.getDateDisponibilite().isEqual(quete.getDatePeremption());
        }

        return false;
    }

    /**
     * Vérifie si l'aventurier possède une spécialité requise pour la quête.
     *
     * @param aventurier l'aventurier à vérifier
     * @param quete la quête
     * @return true si l'aventurier a une spécialité requise ou si aucune spécialité n'est requise
     */
    private boolean aSpecialiteRequise(Aventurier aventurier, Quete quete) {
        // Si la quête ne requiert aucune spécialité, tous les aventuriers sont acceptés
        if (quete.getSpecialitesRequises() == null || quete.getSpecialitesRequises().isEmpty()) {
            return true;
        }

        // Vérifie si la spécialité de l'aventurier est dans la liste des spécialités requises
        if (aventurier.getSpecialite() == null) {
            return false;
        }

        return quete.getSpecialitesRequises().stream()
                .anyMatch(specialite -> specialite.getId().equals(aventurier.getSpecialite().getId()));
    }

    /**
     * Vérifie si le niveau d'expérience de l'aventurier est adapté à la quête.
     * Utilise l'experienceGagnee de la quête pour définir un intervalle optimal.
     *
     * Logique: Un aventurier est adapté si son niveau est dans l'intervalle
     * [experienceGagnee * 0.5, experienceGagnee * 2]
     *
     * @param aventurier l'aventurier à vérifier
     * @param quete la quête
     * @return true si le niveau d'expérience est adapté
     */
    private boolean aNiveauExperienceAdapte(Aventurier aventurier, Quete quete) {
        // Si la quête ne définit pas d'expérience gagnée, accepter tous les niveaux
        if (quete.getExperienceGagnee() == null || quete.getExperienceGagnee() == 0) {
            return true;
        }

        int niveauAventurier = aventurier.getNiveauExperience();
        int experienceQuete = quete.getExperienceGagnee();

        // Définir un intervalle: 50% en dessous à 200% au-dessus de l'XP de la quête
        int niveauMin = (int) (experienceQuete * 0.5);
        int niveauMax = experienceQuete * 2;

        return niveauAventurier >= niveauMin && niveauAventurier <= niveauMax;
    }

    /**
     * Attribue un score à chaque aventurier et trie par score décroissant.
     * Plus le score est élevé, meilleur est le match avec la quête.
     *
     * @param aventuriers la liste des aventuriers éligibles
     * @param quete la quête
     * @return la liste triée par score décroissant
     */
    private List<Aventurier> scorerEtTrier(List<Aventurier> aventuriers, Quete quete) {
        Map<Long, Integer> scores = new HashMap<>();

        // Calculer le score pour chaque aventurier
        for (Aventurier aventurier : aventuriers) {
            int score = calculerScore(aventurier, quete);
            scores.put(aventurier.getId(), score);
        }

        // Trier par score décroissant
        return aventuriers.stream()
                .sorted(Comparator.comparingInt((Aventurier a) -> scores.get(a.getId())).reversed())
                .toList();
    }

    /**
     * Calcule le score d'un aventurier pour une quête donnée.
     * Critères de scoring :
     * - Spécialité matching (50 points)
     * - Niveau d'expérience optimal (jusqu'à 30 points)
     * - Disponibilité immédiate (20 points)
     * - Ratio coût/performance (jusqu'à 20 points)
     *
     * @param aventurier l'aventurier à scorer
     * @param quete la quête
     * @return le score calculé
     */
    private int calculerScore(Aventurier aventurier, Quete quete) {
        int score = 0;

        // 1. Spécialité matching (priorité haute : 50 points)
        if (quete.getSpecialitesRequises() != null && aventurier.getSpecialite() != null) {
            boolean matchSpecialite = quete.getSpecialitesRequises().stream()
                    .anyMatch(s -> s.getId().equals(aventurier.getSpecialite().getId()));
            if (matchSpecialite) {
                score += 50;
            }
        }

        // 2. Niveau d'expérience optimal (jusqu'à 30 points)
        // Plus le niveau est proche de l'XP de la quête, meilleur est le score
        if (quete.getExperienceGagnee() != null && quete.getExperienceGagnee() > 0) {
            int niveauAventurier = aventurier.getNiveauExperience();
            int experienceQuete = quete.getExperienceGagnee();

            // Calculer la proximité au niveau optimal (100% = expérience de la quête)
            double ratio = (double) niveauAventurier / experienceQuete;

            // Score max si ratio entre 0.8 et 1.2 (proche du niveau optimal)
            if (ratio >= 0.8 && ratio <= 1.2) {
                score += 30;
            } else if (ratio >= 0.5 && ratio <= 2.0) {
                // Score dégressif si en dehors de l'intervalle optimal
                score += 15;
            }
        } else {
            // Si pas d'XP définie, donner des points basés sur le niveau global
            score += Math.min(aventurier.getNiveauExperience() / 10, 30);
        }

        // 3. Disponibilité immédiate (20 points)
        if (DISPONIBLE.equals(aventurier.getDisponibilite())) {
            score += 20;
        }

        // 4. Ratio coût/performance (jusqu'à 20 points)
        // Un taux journalier bas donne un meilleur score
        if (aventurier.getTauxJournalierBase() != null) {
            int tauxJournalier = aventurier.getTauxJournalierBase().intValue();
            // Inverser le ratio : plus c'est cher, moins on donne de points
            score += Math.max(0, 20 - (tauxJournalier / 50));
        }

        return score;
    }

    /**
     * Construit le DTO de réponse avec les informations de l'équipe suggérée.
     *
     * @param equipe la liste des aventuriers sélectionnés
     * @param quete la quête
     * @return le {@link TeamSuggestionDTO}
     */
    private TeamSuggestionDTO construireReponse(List<Aventurier> equipe, Quete quete) {
        List<Long> aventurierIds = equipe.stream()
                .map(Aventurier::getId)
                .toList();

        // Calculer le coût total estimé
        double coutTotal = equipe.stream()
                .filter(a -> a.getTauxJournalierBase() != null)
                .mapToDouble(a -> a.getTauxJournalierBase().doubleValue() * (quete.getDureeEstimee() != null ? quete.getDureeEstimee() : 1))
                .sum();

        // Calculer le score moyen
        double scoreMoyen = equipe.stream()
                .mapToInt(a -> calculerScore(a, quete))
                .average()
                .orElse(0.0);

        return new TeamSuggestionDTO(
                aventurierIds,
                equipe.size(),
                coutTotal,
                scoreMoyen
        );
    }
}
