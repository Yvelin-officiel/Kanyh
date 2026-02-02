package com.data.kanyh.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReposSamDTO {
    private Integer dureeReposJours;
    private LocalDate dateDebutRepos;
    private LocalDate dateFinRepos;
    private Long derniereMissionId;
    private String derniereMissionNom;
}
