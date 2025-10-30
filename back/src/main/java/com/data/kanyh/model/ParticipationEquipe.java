package com.data.kanyh.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "participation_equipe")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationEquipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "equipe_id", nullable = false)
    private Equipe equipe;

    @ManyToOne
    @JoinColumn(name = "aventurier_id", nullable = false)
    private Aventurier aventurier;

    private LocalDate dateAffectation;
    private LocalDate dateRetour;
    private String etat;
    private Integer gainExperience;
}
