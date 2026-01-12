package com.data.kanyh.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    @NotNull(message = "Le montant ne peut pas être null")
    private Double montant;

    private String description;
}
