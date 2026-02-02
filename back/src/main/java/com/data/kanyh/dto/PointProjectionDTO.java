package com.data.kanyh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointProjectionDTO {

    private LocalDate date;
    private Double solde;
    private String evenement;
}
