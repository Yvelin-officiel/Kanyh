package com.data.kanyh.controller;

import com.data.kanyh.dto.*;
import com.data.kanyh.model.TypeTransaction;
import com.data.kanyh.service.CompteGuildeService;
import com.data.kanyh.service.TransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compte-guilde")
@AllArgsConstructor
public class CompteGuildeController {

    private final CompteGuildeService compteGuildeService;
    private final TransactionService transactionService;

    /**
     * Récupère le solde actuel du compte de la guilde.
     *
     * @return le {@link CompteGuildeDTO} contenant les informations du compte
     */
    @GetMapping
    public ResponseEntity<CompteGuildeDTO> getCompteGuilde() {
        return ResponseEntity.ok(compteGuildeService.getCompteGuilde());
    }

    /**
     * Effectue une transaction sur le compte de la guilde.
     * Pour ajouter de l'argent, utiliser un montant positif.
     * Pour effectuer une dépense, utiliser un montant négatif.
     *
     * @param transactionDTO le {@link TransactionDTO} contenant le montant de la transaction
     * @return le {@link CompteGuildeDTO} mis à jour
     */
    @PutMapping("/transaction")
    public ResponseEntity<CompteGuildeDTO> effectuerTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(compteGuildeService.effectuerTransaction(transactionDTO));
    }

    /**
     * Récupère le dashboard financier complet avec statistiques et dernières transactions
     *
     * @return le {@link DashboardFinancierDTO} contenant toutes les données du dashboard
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardFinancierDTO> getDashboard() {
        return ResponseEntity.ok(compteGuildeService.getDashboard());
    }

    /**
     * Récupère les projections financières sur N jours avec 3 scénarios
     *
     * @param jours nombre de jours pour la projection (défaut: 30)
     * @return le {@link ProjectionFinanciereDTO} avec les 3 scénarios
     */
    @GetMapping("/projections")
    public ResponseEntity<ProjectionFinanciereDTO> getProjections(
            @RequestParam(required = false, defaultValue = "30") Integer jours) {
        return ResponseEntity.ok(compteGuildeService.getProjections(jours));
    }

    /**
     * Récupère l'historique des transactions avec pagination
     *
     * @param page numéro de page (défaut: 0)
     * @param size taille de la page (défaut: 20)
     * @return une page de {@link TransactionDTO}
     */
    @GetMapping("/transactions")
    public ResponseEntity<Page<TransactionDTO>> getTransactions(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(transactionService.getTransactions(pageable));
    }

    /**
     * Récupère les transactions filtrées par type
     *
     * @param type le type de transaction (PRIME, SALAIRE, REPARATION, ACHAT, AUTRE)
     * @return une liste de {@link TransactionDTO}
     */
    @GetMapping("/transactions/type/{type}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByType(@PathVariable String type) {
        TypeTransaction typeTransaction = TypeTransaction.valueOf(type.toUpperCase());
        return ResponseEntity.ok(transactionService.getTransactionsByType(typeTransaction));
    }

    /**
     * Crée une transaction manuelle
     *
     * @param input le {@link TransactionInputDTO} contenant les détails de la transaction
     * @return le {@link TransactionDTO} de la transaction créée
     */
    @PostMapping("/transactions")
    public ResponseEntity<TransactionDTO> creerTransaction(@Valid @RequestBody TransactionInputDTO input) {
        return ResponseEntity.ok(transactionService.creerTransaction(input));
    }

    /**
     * Récupère le détail d'une transaction
     *
     * @param id l'identifiant de la transaction
     * @return le {@link TransactionDTO} de la transaction
     */
    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }
}
