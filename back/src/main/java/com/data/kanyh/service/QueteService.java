package com.data.kanyh.service;

import com.data.kanyh.dto.QueteDTO;
import com.data.kanyh.dto.QueteInputDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.QueteMapper;
import com.data.kanyh.model.Quete;
import com.data.kanyh.repository.AventurierRepository;
import com.data.kanyh.repository.QueteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class QueteService {

    private final QueteRepository queteRepository;
    private final AventurierRepository aventurierRepository;
    private final QueteMapper queteMapper;
    private static final String NOT_FOUND = "Quête non trouvée";

    /**
     * Récupère toutes les quêtes disponibles.
     * @return une liste de {@link QueteDTO} contenant toutes les quêtes
     */
    public List<QueteDTO> getAllQuetes() {
        return queteRepository.findAll().stream()
                .map(queteMapper::toDTO)
                .toList();
    }

    /**
     * Récupère toutes les quêtes associées à un commanditaire spécifique.
     * @param id l'identifiant du commanditaire
     * @return une liste de {@link QueteDTO} associées au commanditaire
     */
    public List<QueteDTO> getAllByCommanditaireId(Long id) {
        return queteRepository.findByCommanditaireId(id).stream()
                .map(queteMapper::toDTO)
                .toList();
    }

    /**
     * Récupère une quête par son identifiant.
     * @param id l'identifiant de la quête à récupérer
     * @return le {@link QueteDTO} correspondant à l'identifiant fourni
     * @throws NotFoundException si aucune quête n'existe avec cet identifiant
     */
    public QueteDTO getQueteById(Long id) {
        return queteRepository.findById(id)
                .map(queteMapper::toDTO)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    /**
     * Crée et sauvegarde une nouvelle quête.
     * <p>
     * Le statut de la quête créée est automatiquement défini à {@code NOUVELLE}
     * par le mapper, indépendamment du statut fourni dans le DTO d'entrée.
     * </p>
     *
     * @param input le {@link QueteInputDTO} contenant les informations de la quête à créer
     * @return le {@link QueteDTO} de la quête créée avec son identifiant généré
     */
    public QueteDTO save(QueteInputDTO input) {
        Quete quete = queteRepository.save(queteMapper.toEntity(input));
        return queteMapper.toDTO(quete);
    }

    /**
     * Met à jour une quête existante.
     * <p>
     * Cette méthode effectue une mise à jour partielle : seuls les champs non-null
     * du DTO d'entrée sont appliqués à l'entité existante. Les autres champs conservent
     * leur valeur actuelle.
     * </p>
     *
     * @param id    l'identifiant de la quête à mettre à jour
     * @param input le {@link QueteInputDTO} contenant les nouvelles valeurs
     * @return le {@link QueteDTO} de la quête mise à jour
     * @throws NotFoundException si aucune quête n'existe avec cet identifiant
     */
    public QueteDTO updateQuete(Long id, QueteInputDTO input) {
        Quete quete = queteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        queteMapper.updateEntityFromDTO(input, quete);
        return queteMapper.toDTO(queteRepository.save(quete));
    }

    /**
     * Supprime une quête existante.
     * <p>
     * Vérifie d'abord l'existence de la quête avant de procéder à la suppression.
     * </p>
     *
     * @param id l'identifiant de la quête à supprimer
     * @throws NotFoundException si aucune quête n'existe avec cet identifiant
     */
    public void deleteQuete(Long id) {
        if (!queteRepository.existsById(id)) {
            throw new NotFoundException(NOT_FOUND);
        }
        queteRepository.deleteById(id);
    }

    public List<QueteDTO> getQuetesByAventurierId(Long id) {
        if (!aventurierRepository.existsById(id)) {
            throw new NotFoundException("Aventurier non trouvé");
        }

        return queteRepository.findQuetesByAventurierId(id)
                .stream()
                .map(queteMapper::toDTO)
                .toList();
    }
}
