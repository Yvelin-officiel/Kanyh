package com.data.kanyh.service;

import com.data.kanyh.dto.CompteGuildeDTO;
import com.data.kanyh.dto.TransactionDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.CompteGuildeMapper;
import com.data.kanyh.model.CompteGuilde;
import com.data.kanyh.repository.CompteGuildeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CompteGuildeService {

    private final CompteGuildeRepository compteGuildeRepository;
    private final CompteGuildeMapper compteGuildeMapper;
    private static final String NOT_FOUND = "Compte guilde non trouvé";

    /**
     * Récupère le compte de la guilde ou le crée s'il n'existe pas.
     * Il ne peut y avoir qu'un seul compte guilde (ID = 1).
     *
     * @return le {@link CompteGuildeDTO} du compte de la guilde
     */
    public CompteGuildeDTO getCompteGuilde() {
        CompteGuilde compte = compteGuildeRepository.findById(1L)
                .orElseGet(() -> {
                    CompteGuilde nouveauCompte = new CompteGuilde();
                    nouveauCompte.setId(1L);
                    nouveauCompte.setSoldeTotal(0.0);
                    nouveauCompte.setDateMiseAJour(LocalDateTime.now());
                    return compteGuildeRepository.save(nouveauCompte);
                });
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
}
