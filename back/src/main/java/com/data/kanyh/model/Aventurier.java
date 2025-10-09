package com.data.kanyh.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private String specialite;
    private int niveau;
    private Number tauxJournalierBase;
    private String disponibilite;
    private LocalDate dateDebut;
}
