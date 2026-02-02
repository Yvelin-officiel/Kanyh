package com.data.kanyh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioProjectionDTO {

    private String scenario;
    private Double soldeProjet;
    private Double revenuProjet;
    private Double depenseProjetee;
    private List<PointProjectionDTO> evolutionSolde;
    private Double tauxSuccesUtilise;
}
