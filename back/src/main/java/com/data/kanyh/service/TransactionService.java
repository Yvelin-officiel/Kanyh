package com.data.kanyh.service;

import com.data.kanyh.dto.StatistiquesDTO;
import com.data.kanyh.dto.TransactionDTO;
import com.data.kanyh.dto.TransactionInputDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.TransactionMapper;
import com.data.kanyh.model.CompteGuilde;
import com.data.kanyh.model.Transaction;
import com.data.kanyh.model.TypeTransaction;
import com.data.kanyh.repository.CompteGuildeRepository;
import com.data.kanyh.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CompteGuildeRepository compteGuildeRepository;
    private final TransactionMapper transactionMapper;

    /**
     * Crée une transaction manuelle
     */
    @Transactional
    public TransactionDTO creerTransaction(TransactionInputDTO input) {
        CompteGuilde compte = compteGuildeRepository.findById(1L)
            .orElseThrow(() -> new NotFoundException("Compte guilde non trouvé"));

        Double nouveauSolde = compte.getSoldeTotal() + input.getMontant();

        if (nouveauSolde < 0) {
            throw new IllegalArgumentException("Le solde ne peut pas être négatif. Solde actuel: "
                + compte.getSoldeTotal() + ", Montant de la transaction: " + input.getMontant());
        }

        Transaction transaction = transactionMapper.toEntity(input);
        transaction.setInitiePar("UTILISATEUR");
        transaction.setSoldeApres(nouveauSolde);

        compte.setSoldeTotal(nouveauSolde);
        compte.setDateMiseAJour(LocalDateTime.now());
        compteGuildeRepository.save(compte);

        Transaction saved = transactionRepository.save(transaction);
        log.info("Transaction créée: {} {} PO, nouveau solde: {} PO",
            saved.getType(), saved.getMontant(), nouveauSolde);

        return transactionMapper.toDTO(saved);
    }

    /**
     * Méthode package-private pour les transactions automatiques du workflow
     */
    @Transactional
    TransactionDTO enregistrerTransactionAutomatique(TypeTransaction type, Double montant,
                                                      String description, Long queteId,
                                                      Long aventurierId, Long equipementId) {
        CompteGuilde compte = compteGuildeRepository.findById(1L)
            .orElseThrow(() -> new NotFoundException("Compte guilde non trouvé"));

        Double nouveauSolde = compte.getSoldeTotal() + montant;

        if (nouveauSolde < 0) {
            throw new IllegalArgumentException("Le solde ne peut pas être négatif. Solde actuel: "
                + compte.getSoldeTotal() + ", Montant de la transaction: " + montant);
        }

        Transaction transaction = new Transaction();
        transaction.setType(type);
        transaction.setMontant(montant);
        transaction.setDescription(description);
        transaction.setQueteId(queteId);
        transaction.setAventurierId(aventurierId);
        transaction.setEquipementId(equipementId);
        transaction.setInitiePar("SYSTEME");
        transaction.setSoldeApres(nouveauSolde);

        compte.setSoldeTotal(nouveauSolde);
        compte.setDateMiseAJour(LocalDateTime.now());
        compteGuildeRepository.save(compte);

        Transaction saved = transactionRepository.save(transaction);
        log.info("Transaction automatique créée: {} {} PO, nouveau solde: {} PO",
            saved.getType(), saved.getMontant(), nouveauSolde);

        return transactionMapper.toDTO(saved);
    }

    /**
     * Récupère toutes les transactions avec pagination
     */
    public Page<TransactionDTO> getTransactions(Pageable pageable) {
        return transactionRepository.findAllByOrderByDateTransactionDesc(pageable)
            .map(transactionMapper::toDTO);
    }

    /**
     * Récupère les transactions par type
     */
    public List<TransactionDTO> getTransactionsByType(TypeTransaction type) {
        return transactionRepository.findByTypeOrderByDateTransactionDesc(type)
            .stream()
            .map(transactionMapper::toDTO)
            .toList();
    }

    /**
     * Récupère les transactions par période
     */
    public List<TransactionDTO> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByDateRange(startDate, endDate)
            .stream()
            .map(transactionMapper::toDTO)
            .toList();
    }

    /**
     * Récupère une transaction par ID
     */
    public TransactionDTO getTransactionById(Long id) {
        return transactionRepository.findById(id)
            .map(transactionMapper::toDTO)
            .orElseThrow(() -> new NotFoundException("Transaction non trouvée"));
    }

    /**
     * Calcule les statistiques financières sur 30 jours
     */
    public StatistiquesDTO getStatistiques() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyDaysAgo = now.minusDays(30);

        Double totalRevenus = transactionRepository.getTotalRevenusInDateRange(thirtyDaysAgo, now);
        Double totalDepenses = transactionRepository.getTotalDepensesInDateRange(thirtyDaysAgo, now);
        Integer nombreTransactions = transactionRepository.countByDateRange(thirtyDaysAgo, now);

        // Calcul de la répartition par type
        List<Transaction> transactions = transactionRepository.findByDateRange(thirtyDaysAgo, now);
        Map<String, Double> repartitionParType = new HashMap<>();

        for (Transaction t : transactions) {
            String type = t.getType().name();
            repartitionParType.merge(type, Math.abs(t.getMontant()), Double::sum);
        }

        // Solde moyen (approximation simple)
        CompteGuilde compte = compteGuildeRepository.findById(1L).orElse(null);
        Double soldeMoyen = compte != null ? compte.getSoldeTotal() : 0.0;

        StatistiquesDTO stats = new StatistiquesDTO();
        stats.setTotalRevenus30Jours(totalRevenus);
        stats.setTotalDepenses30Jours(totalDepenses);
        stats.setSoldeMoyen30Jours(soldeMoyen);
        stats.setRepartitionParType(repartitionParType);
        stats.setNombreTransactions30Jours(nombreTransactions);

        return stats;
    }
}
