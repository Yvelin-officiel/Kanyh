package com.data.kanyh.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private LocalDate dateDepart;
    private LocalDate dateRetourPrevue;
    private Double coutTotal;
    private Double ratioRentabilite;

    @ManyToMany
    @JoinTable(
            name = "equipe_aventurier",
            joinColumns = @JoinColumn(name = "equipe_id"),
            inverseJoinColumns = @JoinColumn(name = "aventurier_id")
    )
    private List<Aventurier> aventuriers = new ArrayList<>();
}
