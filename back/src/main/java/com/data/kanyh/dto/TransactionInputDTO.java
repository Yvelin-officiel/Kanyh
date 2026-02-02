package com.data.kanyh.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionInputDTO {

    @NotNull(message = "Le type ne peut pas être null")
    private String type;

    @NotNull(message = "Le montant ne peut pas être null")
    private Double montant;

    private String description;
    private Long queteId;
    private Long aventurierId;
    private Long equipementId;
}
