package com.data.kanyh.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EquipeInputDTO {

    @NotBlank(message = "Le nom de l'équipe est obligatoire")
    private String nom;
}
