package com.data.kanyh.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ParticipationEquipeDTO {

    private Long id;
    private Long equipeId;
    private String equipeNom;
    private Long aventurierId;
    private String aventurierNom;
    private LocalDate dateAffectation;
    private LocalDate dateRetour;
    private String etat;
    private Integer gainExperience;
}
