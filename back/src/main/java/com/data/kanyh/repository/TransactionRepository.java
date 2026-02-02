package com.data.kanyh.repository;

import com.data.kanyh.model.Transaction;
import com.data.kanyh.model.TypeTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Récupère toutes les transactions triées par date décroissante
     */
    List<Transaction> findAllByOrderByDateTransactionDesc();

    /**
     * Récupère les transactions avec pagination
     */
    Page<Transaction> findAllByOrderByDateTransactionDesc(Pageable pageable);

    /**
     * Récupère les transactions par type
     */
    List<Transaction> findByTypeOrderByDateTransactionDesc(TypeTransaction type);

    /**
     * Récupère les transactions dans une plage de dates
     */
    @Query("SELECT t FROM Transaction t WHERE t.dateTransaction BETWEEN :startDate AND :endDate ORDER BY t.dateTransaction DESC")
    List<Transaction> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Récupère les transactions d'une quête
     */
    List<Transaction> findByQueteIdOrderByDateTransactionDesc(Long queteId);

    /**
     * Calcule le total des revenus dans une plage de dates
     */
    @Query("SELECT COALESCE(SUM(t.montant), 0) FROM Transaction t WHERE t.montant > 0 AND t.dateTransaction BETWEEN :startDate AND :endDate")
    Double getTotalRevenusInDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Calcule le total des dépenses dans une plage de dates
     */
    @Query("SELECT COALESCE(SUM(ABS(t.montant)), 0) FROM Transaction t WHERE t.montant < 0 AND t.dateTransaction BETWEEN :startDate AND :endDate")
    Double getTotalDepensesInDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Compte le nombre de transactions dans une plage de dates
     */
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.dateTransaction BETWEEN :startDate AND :endDate")
    Integer countByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
