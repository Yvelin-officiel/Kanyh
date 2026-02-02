package com.data.kanyh.service;

import com.data.kanyh.model.ParticipationEquipe;
import com.data.kanyh.model.Quete;
import com.data.kanyh.model.TypeTransaction;
import com.data.kanyh.repository.EquipeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class FinancialWorkflowService {

    private final TransactionService transactionService;
    private final EquipeRepository equipeRepository;

    /**
     * Traite le passage d'une quête à EN_COURS : verse l'acompte de 20%
     */
    @Transactional
    public void traiterQueteEnCours(Quete quete) {
        if (quete.getPrime() == null || quete.getPrime() <= 0) {
            log.warn("Quête {} sans prime définie, pas d'acompte versé", quete.getId());
            return;
        }

        Double acompte = quete.getPrime() * 0.2;
        String description = String.format("Acompte 20%% pour quête '%s'", quete.getNom());

        transactionService.enregistrerTransactionAutomatique(
            TypeTransaction.PRIME,
            acompte,
            description,
            quete.getId(),
            null,
            null
        );

        log.info("Acompte de {} PO versé pour la quête {}", acompte, quete.getId());
    }

    /**
     * Traite le passage d'une quête à TERMINEE : verse le solde de 80% et paie les salaires complets
     */
    @Transactional
    public void traiterQueteTerminee(Quete quete) {
        // 1. Verser le solde de 80% de la prime
        enregistrerSoldePrime(quete);

        // 2. Payer les salaires complets (100%)
        payerSalaires(quete, 1.0);

        // 3. Payer les réparations (stub pour l'instant)
        payerReparations(quete);
    }

    /**
     * Traite le passage d'une quête à ECHOUEE : pas de solde, salaires avec malus 40%
     */
    @Transactional
    public void traiterQueteEchouee(Quete quete) {
        // 1. Pas de versement du solde de 80% (déjà reçu l'acompte)
        log.info("Quête {} échouée, pas de versement du solde de prime", quete.getId());

        // 2. Payer les salaires avec malus 40% (60% du salaire normal)
        payerSalaires(quete, 0.6);

        // 3. Payer les réparations (stub pour l'instant)
        payerReparations(quete);
    }

    /**
     * Verse le solde de 80% de la prime
     */
    private void enregistrerSoldePrime(Quete quete) {
        if (quete.getPrime() == null || quete.getPrime() <= 0) {
            log.warn("Quête {} sans prime définie, pas de solde versé", quete.getId());
            return;
        }

        Double solde = quete.getPrime() * 0.8;
        String description = String.format("Solde 80%% pour quête '%s'", quete.getNom());

        transactionService.enregistrerTransactionAutomatique(
            TypeTransaction.PRIME,
            solde,
            description,
            quete.getId(),
            null,
            null
        );

        log.info("Solde de {} PO versé pour la quête {}", solde, quete.getId());
    }

    /**
     * Paie les salaires de l'équipe avec un multiplicateur (1.0 = complet, 0.6 = avec malus)
     */
    private void payerSalaires(Quete quete, double multiplicateur) {
        if (quete.getEquipeId() == null) {
            log.warn("Quête {} sans équipe assignée, pas de salaires à payer", quete.getId());
            return;
        }

        equipeRepository.findById(quete.getEquipeId()).ifPresentOrElse(equipe -> {
            List<ParticipationEquipe> participations = equipe.getParticipations();

            if (participations.isEmpty()) {
                log.warn("Équipe {} sans membres, pas de salaires à payer", equipe.getId());
                return;
            }

            for (ParticipationEquipe participation : participations) {
                try {
                    // Calculer les jours travaillés
                    LocalDate dateDebut = participation.getDateAffectation() != null
                        ? participation.getDateAffectation()
                        : equipe.getDateDepart() != null ? equipe.getDateDepart() : LocalDate.now();

                    LocalDate dateFin = participation.getDateRetour() != null
                        ? participation.getDateRetour()
                        : LocalDate.now();

                    long joursTravailler = ChronoUnit.DAYS.between(dateDebut, dateFin);
                    if (joursTravailler < 0) {
                        joursTravailler = 0;
                    }

                    // Calculer le salaire
                    Double tauxJournalier = participation.getAventurier().getTauxJournalierBase();
                    if (tauxJournalier == null) {
                        log.warn("Aventurier {} sans taux journalier défini", participation.getAventurier().getId());
                        continue;
                    }

                    Double salaireBrut = tauxJournalier * joursTravailler;
                    Double salaireNet = salaireBrut * multiplicateur;

                    String description;
                    if (multiplicateur < 1.0) {
                        description = String.format("Salaire avec malus pour %s (%d jours, échec)",
                            participation.getAventurier().getNom(), joursTravailler);
                    } else {
                        description = String.format("Salaire pour %s (%d jours)",
                            participation.getAventurier().getNom(), joursTravailler);
                    }

                    transactionService.enregistrerTransactionAutomatique(
                        TypeTransaction.SALAIRE,
                        -salaireNet, // Négatif car c'est une dépense
                        description,
                        quete.getId(),
                        participation.getAventurier().getId(),
                        null
                    );

                    log.info("Salaire de {} PO payé à l'aventurier {} pour la quête {}",
                        salaireNet, participation.getAventurier().getId(), quete.getId());

                } catch (Exception e) {
                    log.error("Erreur lors du paiement du salaire pour l'aventurier {}: {}",
                        participation.getAventurier().getId(), e.getMessage());
                }
            }
        }, () -> log.warn("Équipe {} non trouvée pour la quête {}", quete.getEquipeId(), quete.getId()));
    }

    /**
     * Paie les réparations d'équipement (stub pour future implémentation)
     */
    private void payerReparations(Quete quete) {
        // TODO: À implémenter quand le lien équipement-quête sera créé
        log.debug("Paiement des réparations pour la quête {} (non implémenté)", quete.getId());
    }
}
