package com.data.kanyh.service;

import com.data.kanyh.dto.EquipeDTO;
import com.data.kanyh.dto.EquipeInputDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.EquipeMapper;
import com.data.kanyh.model.Equipe;
import com.data.kanyh.repository.EquipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EquipeService {

    private final EquipeRepository equipeRepository;
    private final EquipeMapper equipeMapper;
    private static final String NOT_FOUND = "Équipe non trouvée";

    /**
     * Récupère toutes les équipes disponibles.
     *
     * @return une liste de {@link EquipeDTO} contenant toutes les équipes
     */
    public List<EquipeDTO> getAllEquipes() {
        return equipeRepository.findAll().stream()
                .map(equipeMapper::toDTO)
                .toList();
    }

    /**
     * Récupère une équipe par son identifiant.
     *
     * @param id l'identifiant de l'équipe à récupérer
     * @return le {@link EquipeDTO} correspondant à l'identifiant fourni
     * @throws NotFoundException si aucune équipe n'existe avec cet identifiant
     */
    public EquipeDTO getEquipeById(Long id) {
        return equipeRepository.findById(id)
                .map(equipeMapper::toDTO)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    /**
     * Crée et sauvegarde une nouvelle équipe.
     * Note: Cette méthode ne crée que l'équipe. Les participations des aventuriers doivent être créées
     * séparément via le ParticipationEquipeService.
     *
     * @param input le {@link EquipeInputDTO} contenant les informations de l'équipe à créer
     * @return le {@link EquipeDTO} de l'équipe créée avec son identifiant généré
     */
    public EquipeDTO save(EquipeInputDTO input) {
        Equipe equipe = equipeMapper.toEntity(input);
        Equipe savedEquipe = equipeRepository.save(equipe);
        return equipeMapper.toDTO(savedEquipe);
    }

    /**
     * Met à jour une équipe existante.
     * Note: Cette méthode ne met à jour que les informations de base de l'équipe.
     * Les participations doivent être gérées séparément via le ParticipationEquipeService.
     *
     * @param id l'identifiant de l'équipe à mettre à jour
     * @param input le {@link EquipeInputDTO} contenant les nouvelles valeurs
     * @return le {@link EquipeDTO} de l'équipe mise à jour
     * @throws NotFoundException si l'équipe n'existe pas
     */
    public EquipeDTO updateEquipe(Long id, EquipeInputDTO input) {
        Equipe equipe = equipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        equipeMapper.updateEntityFromDTO(input, equipe);
        Equipe updatedEquipe = equipeRepository.save(equipe);
        return equipeMapper.toDTO(updatedEquipe);
    }

    /**
     * Supprime une équipe existante.
     * Vérifie d'abord l'existence de l'équipe avant de procéder à la suppression.
     *
     * @param id l'identifiant de l'équipe à supprimer
     * @throws NotFoundException si aucune équipe n'existe avec cet identifiant
     */
    public void deleteEquipe(Long id) {
        if (!equipeRepository.existsById(id)) {
            throw new NotFoundException(NOT_FOUND);
        }
        equipeRepository.deleteById(id);
    }

}
