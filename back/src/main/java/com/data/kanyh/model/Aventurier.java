package com.data.kanyh.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aventurier")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Aventurier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private int niveauExperience;
    @ManyToOne
    @JoinColumn(name = "specialite_id")
    private Specialite specialite;
    private Double tauxJournalierBase;
    private String disponibilite;
    private LocalDate dateDisponibilite;

    @OneToMany(mappedBy = "aventurier")
    private List<ParticipationEquipe> participations = new ArrayList<>();
}
