package com.data.kanyh.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AventurierDTO {

    private Long id;
    private String nom;
    private String specialite;
    private int niveauExperience;
    private Number tauxJournalierBase;
    private String disponibilite;
    private LocalDate dateDisponibilite;
}
