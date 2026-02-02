package com.data.kanyh.service;

import com.data.kanyh.dto.QueteDTO;
import com.data.kanyh.dto.QueteInputDTO;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.QueteMapper;
import com.data.kanyh.model.ParticipationEquipe;
import com.data.kanyh.model.Quete;
import com.data.kanyh.model.StatutQuete;
import com.data.kanyh.repository.AventurierRepository;
import com.data.kanyh.repository.EquipeRepository;
import com.data.kanyh.repository.QueteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class QueteService {

    private final QueteRepository queteRepository;
    private final AventurierRepository aventurierRepository;
    private final EquipeRepository equipeRepository;
    private final QueteMapper queteMapper;
    private final ReposSamService reposSamService;
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
     * <p>
     * Si le statut de la quête passe à TERMINEE, le repos SAM est automatiquement
     * appliqué à tous les aventuriers de l'équipe.
     * </p>
     *
     * @param id    l'identifiant de la quête à mettre à jour
     * @param input le {@link QueteInputDTO} contenant les nouvelles valeurs
     * @return le {@link QueteDTO} de la quête mise à jour
     * @throws NotFoundException si aucune quête n'existe avec cet identifiant
     */
    @Transactional
    public QueteDTO updateQuete(Long id, QueteInputDTO input) {
        Quete quete = queteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));

        StatutQuete ancienStatut = quete.getStatut();

        queteMapper.updateEntityFromDTO(input, quete);
        Quete queteUpdated = queteRepository.save(quete);

        // Si la quête passe à TERMINEE, appliquer le repos SAM automatiquement
        if (queteUpdated.getStatut() == StatutQuete.TERMINEE && ancienStatut != StatutQuete.TERMINEE) {
            appliquerReposSurQueteTerminee(queteUpdated);
        }

        return queteMapper.toDTO(queteUpdated);
    }

    /**
     * Applique le repos SAM à tous les aventuriers d'une quête terminée.
     *
     * @param quete la quête terminée
     */
    private void appliquerReposSurQueteTerminee(Quete quete) {
        if (quete.getEquipeId() == null) {
            log.warn("Quête {} terminée sans équipe assignée", quete.getId());
            return;
        }

        equipeRepository.findById(quete.getEquipeId()).ifPresent(equipe -> {
            List<ParticipationEquipe> participations = equipe.getParticipations();

            for (ParticipationEquipe participation : participations) {
                try {
                    // Mettre à jour dateRetour si non définie
                    if (participation.getDateRetour() == null) {
                        participation.setDateRetour(LocalDate.now());
                    }

                    // Appliquer le repos SAM
                    reposSamService.appliquerRepos(
                            participation.getAventurier().getId(),
                            participation,
                            quete
                    );

                    log.info("Repos SAM appliqué à l'aventurier {} pour la quête {}",
                             participation.getAventurier().getId(), quete.getId());
                } catch (Exception e) {
                    log.error("Erreur lors de l'application du repos pour l'aventurier {} : {}",
                             participation.getAventurier().getId(), e.getMessage());
                }
            }
        });
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
