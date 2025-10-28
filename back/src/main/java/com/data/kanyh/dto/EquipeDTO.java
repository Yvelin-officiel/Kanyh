package com.data.kanyh.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EquipeDTO {

    private Long id;
    private String nom;
    private LocalDate dateDepart;
    private LocalDate dateRetourPrevue;
    private Double coutTotal;
    private Double ratioRentabilite;
    private List<ParticipationEquipeDTO> participations;
}
