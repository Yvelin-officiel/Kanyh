package com.data.kanyh.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ParticipationEquipeInputDTO {

    @NotNull(message = "L'ID de l'équipe est obligatoire")
    private Long equipeId;

    @NotNull(message = "L'ID de l'aventurier est obligatoire")
    private Long aventurierId;

    @NotNull(message = "La date d'affectation est obligatoire")
    private LocalDate dateAffectation;

    private LocalDate dateRetour;

    private String etat;

    private Integer gainExperience;
}
