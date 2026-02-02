package com.data.kanyh.dto;

import lombok.Data;

import java.util.List;

@Data
public class AventurierDetailDTO {
    // Informations de base
    private AventurierDTO aventurier;

    // Missions
    private List<MissionHistoriqueDTO> missionsEnCours;
    private List<MissionHistoriqueDTO> missionsPassees;

    // Repos
    private ReposSamDTO reposActuel;
    private Integer joursTotauxRepos;
}
