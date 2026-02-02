package com.data.kanyh.mapper;

import com.data.kanyh.dto.TransactionDTO;
import com.data.kanyh.dto.TransactionInputDTO;
import com.data.kanyh.model.Transaction;
import com.data.kanyh.model.TypeTransaction;
import com.data.kanyh.repository.AventurierRepository;
import com.data.kanyh.repository.EquipementRepository;
import com.data.kanyh.repository.QueteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransactionMapper {

    private final QueteRepository queteRepository;
    private final AventurierRepository aventurierRepository;
    private final EquipementRepository equipementRepository;

    /**
     * Convertit une entité Transaction en DTO avec dénormalisation des noms
     */
    public TransactionDTO toDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setType(transaction.getType() != null ? transaction.getType().name() : null);
        dto.setMontant(transaction.getMontant());
        dto.setDescription(transaction.getDescription());
        dto.setDateTransaction(transaction.getDateTransaction());
        dto.setQueteId(transaction.getQueteId());
        dto.setAventurierId(transaction.getAventurierId());
        dto.setEquipementId(transaction.getEquipementId());
        dto.setInitiePar(transaction.getInitiePar());
        dto.setSoldeApres(transaction.getSoldeApres());

        // Dénormalisation des noms
        if (transaction.getQueteId() != null) {
            queteRepository.findById(transaction.getQueteId())
                .ifPresent(quete -> dto.setQueteNom(quete.getNom()));
        }

        if (transaction.getAventurierId() != null) {
            aventurierRepository.findById(transaction.getAventurierId())
                .ifPresent(aventurier -> dto.setAventurierNom(aventurier.getNom()));
        }

        if (transaction.getEquipementId() != null) {
            equipementRepository.findById(transaction.getEquipementId())
                .ifPresent(equipement -> dto.setEquipementNom(equipement.getNom()));
        }

        return dto;
    }

    /**
     * Convertit un DTO d'entrée en entité Transaction
     */
    public Transaction toEntity(TransactionInputDTO dto) {
        if (dto == null) {
            return null;
        }

        Transaction transaction = new Transaction();
        transaction.setType(TypeTransaction.valueOf(dto.getType()));
        transaction.setMontant(dto.getMontant());
        transaction.setDescription(dto.getDescription());
        transaction.setQueteId(dto.getQueteId());
        transaction.setAventurierId(dto.getAventurierId());
        transaction.setEquipementId(dto.getEquipementId());

        return transaction;
    }
}
