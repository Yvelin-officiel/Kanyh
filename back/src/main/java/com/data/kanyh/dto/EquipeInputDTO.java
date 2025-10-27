package com.data.kanyh.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class EquipeInputDTO {

    @NotBlank(message = "Le nom de l'équipe est obligatoire")
    private String nom;

    @NotNull(message = "La liste des aventuriers est obligatoire")
    @Size(min = 1, message = "L'équipe doit contenir au moins un aventurier")
    private List<Long> aventuriers;
}
