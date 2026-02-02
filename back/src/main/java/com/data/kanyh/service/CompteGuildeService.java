package com.data.kanyh.service;

import com.data.kanyh.dto.*;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.CompteGuildeMapper;
import com.data.kanyh.model.*;
import com.data.kanyh.repository.CompteGuildeRepository;
import com.data.kanyh.repository.EquipeRepository;
import com.data.kanyh.repository.QueteRepository;
import com.data.kanyh.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompteGuildeService {

    private final CompteGuildeRepository compteGuildeRepository;
    private final CompteGuildeMapper compteGuildeMapper;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final QueteRepository queteRepository;
    private final EquipeRepository equipeRepository;
    private static final String NOT_FOUND = "Compte guilde non trouvé";

    /**
     * Récupère le compte de la guilde ou le crée s'il n'existe pas.
     * Il ne peut y avoir qu'un seul compte guilde (ID = 1).
     *
     * @return le {@link CompteGuildeDTO} du compte de la guilde
     */
    public CompteGuildeDTO getCompteGuilde() {
        CompteGuilde compte = compteGuildeRepository.findById(1L).orElse(null);
        if (compte == null) {
            CompteGuilde nouveauCompte = new CompteGuilde();
            nouveauCompte.setSoldeTotal(0.0);
            nouveauCompte.setDateMiseAJour(LocalDateTime.now());
            compte = compteGuildeRepository.save(nouveauCompte);
        }
        return compteGuildeMapper.toDTO(compte);
    }

    /**
     * Effectue une transaction sur le compte de la guilde.
     * Un montant positif ajoute de l'argent, un montant négatif effectue une dépense.
     *
     * @param transactionDTO le {@link TransactionDTO} contenant le montant de la transaction
     * @return le {@link CompteGuildeDTO} mis à jour
     * @throws IllegalArgumentException si le solde devient négatif après la transaction
     */
    public CompteGuildeDTO effectuerTransaction(TransactionDTO transactionDTO) {
        CompteGuilde compte = compteGuildeRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));

        Double nouveauSolde = compte.getSoldeTotal() + transactionDTO.getMontant();

        if (nouveauSolde < 0) {
            throw new IllegalArgumentException("Le solde ne peut pas être négatif. Solde actuel: "
                    + compte.getSoldeTotal() + ", Montant de la transaction: " + transactionDTO.getMontant());
        }

        compte.setSoldeTotal(nouveauSolde);
        compte.setDateMiseAJour(LocalDateTime.now());

        CompteGuilde updatedCompte = compteGuildeRepository.save(compte);
        return compteGuildeMapper.toDTO(updatedCompte);
    }

    /**
     * Récupère le dashboard financier complet
     */
    public DashboardFinancierDTO getDashboard() {
        CompteGuilde compte = compteGuildeRepository.findById(1L).orElse(null);
        if (compte == null) {
            // Créer le compte en utilisant la même logique que getCompteGuilde
            CompteGuilde nouveauCompte = new CompteGuilde();
            nouveauCompte.setSoldeTotal(0.0);
            nouveauCompte.setDateMiseAJour(LocalDateTime.now());
            compte = compteGuildeRepository.save(nouveauCompte);
        }

        StatistiquesDTO statistiques = transactionService.getStatistiques();

        Pageable pageable = PageRequest.of(0, 10);
        List<TransactionDTO> dernieresTransactions = transactionService.getTransactions(pageable).getContent();

        DashboardFinancierDTO dashboard = new DashboardFinancierDTO();
        dashboard.setSoldeActuel(compte.getSoldeTotal());
        dashboard.setDerniereMiseAJour(compte.getDateMiseAJour());
        dashboard.setStatistiques(statistiques);
        dashboard.setDernieresTransactions(dernieresTransactions);

        return dashboard;
    }

    /**
     * Calcule les projections financières sur N jours avec 3 scénarios
     */
    public ProjectionFinanciereDTO getProjections(Integer joursProjection) {
        if (joursProjection == null || joursProjection <= 0) {
            joursProjection = 30; // Par défaut 30 jours
        }

        CompteGuilde compte = compteGuildeRepository.findById(1L).orElse(null);
        if (compte == null) {
            CompteGuilde nouveauCompte = new CompteGuilde();
            nouveauCompte.setSoldeTotal(0.0);
            nouveauCompte.setDateMiseAJour(LocalDateTime.now());
            compte = compteGuildeRepository.save(nouveauCompte);
        }

        Double soldeActuel = compte.getSoldeTotal();
        LocalDate dateProjection = LocalDate.now().plusDays(joursProjection);

        // Récupérer les quêtes en cours
        List<Quete> quetesEnCours = queteRepository.findAll().stream()
            .filter(q -> q.getStatut() == StatutQuete.EN_COURS)
            .collect(Collectors.toList());

        // Calculer les détails de chaque quête
        List<QueteProjectionDTO> queteProjections = new ArrayList<>();
        for (Quete quete : quetesEnCours) {
            QueteProjectionDTO projection = calculerQueteProjection(quete);
            if (projection != null) {
                queteProjections.add(projection);
            }
        }

        // Créer les 3 scénarios
        ScenarioProjectionDTO optimiste = creerScenario("OPTIMISTE", soldeActuel, queteProjections, 0.9, 0.8, 1.0);
        ScenarioProjectionDTO realiste = creerScenario("REALISTE", soldeActuel, queteProjections, 0.6, 1.0, 1.1);
        ScenarioProjectionDTO pessimiste = creerScenario("PESSIMISTE", soldeActuel, queteProjections, 0.3, 1.2, 1.2);

        ProjectionFinanciereDTO projection = new ProjectionFinanciereDTO();
        projection.setSoldeActuel(soldeActuel);
        projection.setDateProjection(dateProjection);
        projection.setOptimiste(optimiste);
        projection.setRealiste(realiste);
        projection.setPessimiste(pessimiste);
        projection.setQuetesEnCours(queteProjections);

        return projection;
    }

    /**
     * Calcule les détails financiers d'une quête en cours
     */
    private QueteProjectionDTO calculerQueteProjection(Quete quete) {
        if (quete.getEquipeId() == null) {
            return null;
        }

        QueteProjectionDTO projection = new QueteProjectionDTO();
        projection.setQueteId(quete.getId());
        projection.setQueteNom(quete.getNom());
        projection.setPrime(quete.getPrime() != null ? quete.getPrime() : 0.0);

        // Date de retour prévue
        LocalDate dateRetourPrevue = quete.getDateCreation() != null && quete.getDureeEstimee() != null
            ? quete.getDateCreation().plusDays(quete.getDureeEstimee())
            : LocalDate.now();
        projection.setDateRetourPrevue(dateRetourPrevue);

        // Durée restante
        long dureeRestante = ChronoUnit.DAYS.between(LocalDate.now(), dateRetourPrevue);
        projection.setDureeRestante((int) Math.max(0, dureeRestante));

        // Calculer les salaires
        Double salaireTotal = 0.0;
        if (quete.getEquipeId() != null) {
            equipeRepository.findById(quete.getEquipeId()).ifPresent(equipe -> {
                Double[] total = {0.0};
                for (ParticipationEquipe participation : equipe.getParticipations()) {
                    if (participation.getAventurier().getTauxJournalierBase() != null) {
                        total[0] += participation.getAventurier().getTauxJournalierBase() * Math.max(0, dureeRestante);
                    }
                }
                projection.setSalaireTotal(total[0]);
            });
            salaireTotal = projection.getSalaireTotal() != null ? projection.getSalaireTotal() : 0.0;
        } else {
            projection.setSalaireTotal(0.0);
        }

        // Estimation des réparations (30% du coût moyen)
        Double reparationEstimee = salaireTotal * 0.3;
        projection.setReparationEstimee(reparationEstimee);

        // Dépense nette et bénéfice
        Double depenseNette = salaireTotal + reparationEstimee;
        projection.setDepenseNette(depenseNette);

        Double beneficeNet = projection.getPrime() - depenseNette;
        projection.setBeneficeNet(beneficeNet);

        return projection;
    }

    /**
     * Crée un scénario de projection
     */
    private ScenarioProjectionDTO creerScenario(String nom, Double soldeActuel,
                                                 List<QueteProjectionDTO> quetes,
                                                 double tauxSucces, double coeffReparation,
                                                 double coeffDelai) {
        ScenarioProjectionDTO scenario = new ScenarioProjectionDTO();
        scenario.setScenario(nom);
        scenario.setTauxSuccesUtilise(tauxSucces);

        Double revenuProjet = 0.0;
        Double depenseProjetee = 0.0;

        List<PointProjectionDTO> evolution = new ArrayList<>();
        evolution.add(new PointProjectionDTO(LocalDate.now(), soldeActuel, "Solde actuel"));

        Double soldeCourant = soldeActuel;

        // Trier les quêtes par date de retour
        List<QueteProjectionDTO> quetesTriees = quetes.stream()
            .sorted(Comparator.comparing(QueteProjectionDTO::getDateRetourPrevue))
            .collect(Collectors.toList());

        for (QueteProjectionDTO quete : quetesTriees) {
            // Appliquer le taux de succès et les coefficients
            Double revenu = quete.getPrime() * 0.8 * tauxSucces; // 80% car 20% déjà versé
            Double depense = (quete.getSalaireTotal() + quete.getReparationEstimee() * coeffReparation);

            if (tauxSucces < 1.0) {
                // En cas d'échec partiel, pas de revenu mais salaires avec malus
                depense *= 0.6;
            }

            revenuProjet += revenu;
            depenseProjetee += depense;

            soldeCourant = soldeCourant + revenu - depense;

            LocalDate dateAjustee = quete.getDateRetourPrevue().plusDays((long) (quete.getDureeRestante() * (coeffDelai - 1)));
            evolution.add(new PointProjectionDTO(dateAjustee, soldeCourant, "Fin quête: " + quete.getQueteNom()));
        }

        scenario.setRevenuProjet(revenuProjet);
        scenario.setDepenseProjetee(depenseProjetee);
        scenario.setSoldeProjet(soldeCourant);
        scenario.setEvolutionSolde(evolution);

        return scenario;
    }
}
