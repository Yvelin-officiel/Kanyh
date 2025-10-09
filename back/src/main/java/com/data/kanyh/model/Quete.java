package com.data.kanyh.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "quetes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    private Double prime;
    private Integer dureeEstimee;
    private LocalDate datePeremption;
    private Integer experienceGagnee;

    @Enumerated(EnumType.STRING)
    private StatutQuete statut;

    private Long commanditaireId;
    private Long equipeId;
}

