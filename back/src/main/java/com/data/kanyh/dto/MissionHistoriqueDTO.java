package com.data.kanyh.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MissionHistoriqueDTO {
    private Long id;
    private String nomQuete;
    private String statut;
    private LocalDate dateDebut;
    private LocalDate dateRetour;
    private Integer dureeReelle;
    private Integer experienceGagnee;
    private String etat;
}
