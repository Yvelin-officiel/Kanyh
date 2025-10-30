package com.data.kanyh.mapper;

import com.data.kanyh.dto.EquipeDTO;
import com.data.kanyh.dto.EquipeInputDTO;
import com.data.kanyh.model.Equipe;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class EquipeMapper {

    private final ParticipationEquipeMapper participationEquipeMapper;

    /**
     * Convertit une entité {@link Equipe} en {@link EquipeDTO}.
     * Tous les champs de l'entité sont mappés vers le DTO, y compris la liste complète des participations.
     *
     * @param equipe l'entité {@link Equipe} à convertir
     * @return le {@link EquipeDTO} correspondant
     */
    public EquipeDTO toDTO(Equipe equipe) {
        EquipeDTO dto = new EquipeDTO();
        dto.setId(equipe.getId());
        dto.setNom(equipe.getNom());
        dto.setDateDepart(equipe.getDateDepart());
        dto.setDateRetourPrevue(equipe.getDateRetourPrevue());
        dto.setCoutTotal(equipe.getCoutTotal());
        dto.setRatioRentabilite(equipe.getRatioRentabilite());
        dto.setParticipations(equipe.getParticipations().stream()
                .map(participationEquipeMapper::toDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    /**
     * Convertit un {@link EquipeInputDTO} en entité {@link Equipe}.
     * Cette méthode est utilisée lors de la création d'une nouvelle équipe.
     * Seul le nom est mappé.
     * Les participations doivent être créées séparément via ParticipationEquipeService.
     * Les champs calculés (coutTotal, ratioRentabilite, dates) restent null et devront être définis par la logique métier.
     *
     * @param input le {@link EquipeInputDTO} contenant les données de la nouvelle équipe
     * @return l'entité {@link Equipe} correspondante
     */
    public Equipe toEntity(EquipeInputDTO input) {
        Equipe equipe = new Equipe();
        equipe.setNom(input.getNom());
        return equipe;
    }

    /**
     * Met à jour une entité {@link Equipe} existante avec les valeurs d'un {@link EquipeInputDTO}.
     * Cette méthode effectue une mise à jour : seul le nom est modifié.
     * Les participations doivent être gérées séparément via ParticipationEquipeService.
     * Les champs calculés (coutTotal, ratioRentabilite, dates) ne sont pas modifiés par cette méthode.
     *
     * @param input le {@link EquipeInputDTO} contenant les nouvelles valeurs
     * @param equipe l'entité {@link Equipe} à mettre à jour (modifiée en place)
     */
    public void updateEntityFromDTO(EquipeInputDTO input, Equipe equipe) {
        equipe.setNom(input.getNom());
    }
}
