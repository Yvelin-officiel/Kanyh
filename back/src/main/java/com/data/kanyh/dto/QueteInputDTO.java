package com.data.kanyh.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class QueteInputDTO {
    @NotBlank(message = "Le nom de la quête ne peut pas être vide")
    private String nom;

    @NotBlank(message = "La description de la quête ne peut pas être vide")
    private String description;

    @NotNull(message = "La prime de la quête ne peut pas être nulle")
    @PositiveOrZero(message = "La prime de la quête doit être positive ou zéro")
    private Double prime;

    @NotNull(message = "La durée estimée de la quête ne peut pas être nulle")
    @PositiveOrZero(message = "La durée estimée de la quête doit être positive ou zéro")
    private Integer dureeEstimee;

    @NotNull(message = "La date de péremption de la quête ne peut pas être nulle")
    private LocalDate datePeremption;

    @NotBlank(message = "Le statut de la quête ne peut pas être vide")
    private String statut;

    private List<String> specialitesRequisesIds;
}
