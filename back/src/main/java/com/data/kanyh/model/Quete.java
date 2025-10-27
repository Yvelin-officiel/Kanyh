package com.data.kanyh.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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

    @ManyToMany
    @JoinTable(
        name = "quete_specialites",
        joinColumns = @JoinColumn(name = "quete_id"),
        inverseJoinColumns = @JoinColumn(name = "specialite_id")
    )
    private List<Specialite> specialitesRequises;
}
