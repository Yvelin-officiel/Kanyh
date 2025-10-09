package com.data.kanyh.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class QueteDTO {
    private Long id;
    private String nom;
    private String description;
    private Double prime;
    private Integer dureeEstimee;
    private LocalDate datePeremption;
    private Integer experienceGagnee;
    private String statut;
    private Long commanditaireId;
    private Long equipeId;
}
