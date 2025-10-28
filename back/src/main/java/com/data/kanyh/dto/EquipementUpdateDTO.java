package com.data.kanyh.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class EquipementUpdateDTO {
    @NotNull(message = "L'id est obligatoire")
    private Long id;
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    @NotBlank(message = "Le type est obligatoire")
    private String type;
    @NotNull(message = "La durabilité maximale est obligatoire")
    private int durabiliteMax;
    @NotNull(message = "La durabilité restante est obligatoire")
    private int durabiliteRestante;
    private String disponibilite;
    private String dateRetourPrevue;
    private float coutReparation;
}
