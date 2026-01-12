package com.data.kanyh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO pour la réponse de génération automatique d'équipe.
 * Contient la liste des aventuriers suggérés et des informations utiles pour l'UI.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamSuggestionDTO {

    /**
     * Liste des IDs des aventuriers suggérés pour former l'équipe
     */
    private List<Long> aventurierIds;

    /**
     * Nombre d'aventuriers dans l'équipe suggérée
     */
    private Integer nombreAventuriers;

    /**
     * Coût total estimé de l'équipe (somme des taux journaliers * durée)
     */
    private Double coutTotalEstime;

    /**
     * Score moyen de l'équipe (pour information)
     */
    private Double scoreMoyen;
}
