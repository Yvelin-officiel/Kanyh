package com.data.kanyh.dto;

import lombok.Data;

@Data
public class EquipementDTO {
    private Long id;
    private String nom;
    private String type;
    private int durabiliteMax;
    private int durabiliteRestante;
    private String disponibilite;
    private String dateRetourPrevue;
    private float coutReparation;
}
