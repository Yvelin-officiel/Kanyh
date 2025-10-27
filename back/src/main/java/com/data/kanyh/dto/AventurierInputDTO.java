package com.data.kanyh.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class AventurierInputDTO {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "La spécialité est obligatoire")
    private String specialite;

    @NotNull(message = "Le taux journalier de base est obligatoire")
    @PositiveOrZero(message = "Le taux journalier de base doit être supérieur ou égal à zéro")
    private Number tauxJournalierBase;

}
