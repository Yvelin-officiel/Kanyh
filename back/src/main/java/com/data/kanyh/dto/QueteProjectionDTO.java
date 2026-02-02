package com.data.kanyh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueteProjectionDTO {

    private Long queteId;
    private String queteNom;
    private Double prime;
    private LocalDate dateRetourPrevue;
    private Integer dureeRestante;
    private Double salaireTotal;
    private Double reparationEstimee;
    private Double depenseNette;
    private Double beneficeNet;
}
