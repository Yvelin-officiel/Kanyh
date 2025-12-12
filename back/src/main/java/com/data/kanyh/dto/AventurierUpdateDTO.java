package com.data.kanyh.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AventurierUpdateDTO {

    @NotNull(message = "L'id est obligatoire")
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "La spécialité est obligatoire (id)")
    private String specialiteId;

    @NotNull(message = "Le niveau est obligatoire")
    @PositiveOrZero(message = "Le niveau doit être positif ou nul")
    private Integer niveauExperience;

    @NotNull(message = "Le taux journalier de base est obligatoire")
    @PositiveOrZero(message = "Le taux journalier de base doit être positif ou nul")
    private Double tauxJournalierBase;

    @NotBlank(message = "La disponibilité est obligatoire")
    private String disponibilite;

    @NotNull(message = "La date de disponibilité est obligatoire")
    private LocalDate dateDisponibilite;
}