package com.data.kanyh.controller;

import com.data.kanyh.dto.CompteGuildeDTO;
import com.data.kanyh.dto.TransactionDTO;
import com.data.kanyh.service.CompteGuildeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compte-guilde")
@AllArgsConstructor
public class CompteGuildeController {

    private final CompteGuildeService compteGuildeService;

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
}
