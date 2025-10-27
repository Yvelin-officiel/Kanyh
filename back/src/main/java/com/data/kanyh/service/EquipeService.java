package com.data.kanyh.service;

import com.data.kanyh.dto.EquipeDTO;
import com.data.kanyh.dto.EquipeInputDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.EquipeMapper;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.model.Equipe;
import com.data.kanyh.repository.AventurierRepository;
import com.data.kanyh.repository.EquipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EquipeService {

    private final EquipeRepository equipeRepository;
    private final AventurierRepository aventurierRepository;
    private final EquipeMapper equipeMapper;
    private static final String NOT_FOUND = "Équipe non trouvée";
    private static final String AVENTURIER_NOT_FOUND = "Un ou plusieurs aventuriers non trouvés";

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
     * Valide que tous les aventuriers spécifiés existent avant de créer l'équipe.
     *
     * @param input le {@link EquipeInputDTO} contenant les informations de l'équipe à créer
     * @return le {@link EquipeDTO} de l'équipe créée avec son identifiant généré
     * @throws NotFoundException si un ou plusieurs aventuriers n'existent pas
     */
    public EquipeDTO save(EquipeInputDTO input) {
        List<Aventurier> aventuriers = fetchAventuriers(input.getAventuriers());
        Equipe equipe = equipeMapper.toEntity(input, aventuriers);
        Equipe savedEquipe = equipeRepository.save(equipe);
        return equipeMapper.toDTO(savedEquipe);
    }

    /**
     * Met à jour une équipe existante.
     * Valide que l'équipe et tous les aventuriers spécifiés existent avant la mise à jour.
     *
     * @param id l'identifiant de l'équipe à mettre à jour
     * @param input le {@link EquipeInputDTO} contenant les nouvelles valeurs
     * @return le {@link EquipeDTO} de l'équipe mise à jour
     * @throws NotFoundException si l'équipe n'existe pas ou si un ou plusieurs aventuriers n'existent pas
     */
    public EquipeDTO updateEquipe(Long id, EquipeInputDTO input) {
        Equipe equipe = equipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        List<Aventurier> aventuriers = fetchAventuriers(input.getAventuriers());
        equipeMapper.updateEntityFromDTO(input, equipe, aventuriers);
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

    /**
     * Récupère la liste des aventuriers correspondant aux IDs fournis.
     * Valide que tous les aventuriers existent.
     *
     * @param aventurierIds la liste des IDs des aventuriers à récupérer
     * @return la liste des entités {@link Aventurier} correspondantes
     * @throws NotFoundException si un ou plusieurs aventuriers n'existent pas
     */
    private List<Aventurier> fetchAventuriers(List<Long> aventurierIds) {
        List<Aventurier> aventuriers = aventurierIds.stream()
                .map(id -> aventurierRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(AVENTURIER_NOT_FOUND)))
                .collect(Collectors.toList());
        return aventuriers;
    }
}
