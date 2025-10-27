package com.data.kanyh.dto;

import com.data.kanyh.model.Specialite;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AventurierDTO {

    private Long id;
    private String nom;
    private Specialite specialite;
    private int niveau;
    private Number tauxJournalierBase;
    private String disponibilite;
    private LocalDate dateDebut;
}
