package com.data.kanyh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectionFinanciereDTO {

    private Double soldeActuel;
    private LocalDate dateProjection;
    private ScenarioProjectionDTO optimiste;
    private ScenarioProjectionDTO realiste;
    private ScenarioProjectionDTO pessimiste;
    private List<QueteProjectionDTO> quetesEnCours;
}
