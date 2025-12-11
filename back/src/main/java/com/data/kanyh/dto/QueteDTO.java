package com.data.kanyh.dto;

import com.data.kanyh.model.Specialite;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

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
    private List<Specialite> specialitesRequises;
    private LocalDate dateCreation;
}
