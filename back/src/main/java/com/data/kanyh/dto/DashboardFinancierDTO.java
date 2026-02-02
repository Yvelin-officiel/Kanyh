package com.data.kanyh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardFinancierDTO {

    private Double soldeActuel;
    private LocalDateTime derniereMiseAJour;
    private StatistiquesDTO statistiques;
    private List<TransactionDTO> dernieresTransactions;
}
