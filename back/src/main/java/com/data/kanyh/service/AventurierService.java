package com.data.kanyh.service;

import com.data.kanyh.dto.*;
import com.data.kanyh.exception.NotFoundException;
import com.data.kanyh.mapper.AventurierMapper;
import com.data.kanyh.model.Aventurier;
import com.data.kanyh.model.ParticipationEquipe;
import com.data.kanyh.model.Quete;
import com.data.kanyh.repository.AventurierRepository;
import com.data.kanyh.repository.ParticipationEquipeRepository;
import com.data.kanyh.repository.QueteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AventurierService {

    private final AventurierRepository aventurierRepository;
    private final AventurierMapper aventurierMapper;
    private final QueteRepository queteRepository;
    private final ParticipationEquipeRepository participationEquipeRepository;

    private static final String NOT_FOUND = "Aventurier non trouvé";

    public AventurierService(AventurierRepository aventurierRepository,
                            AventurierMapper aventurierMapper,
                            QueteRepository queteRepository,
                            ParticipationEquipeRepository participationEquipeRepository) {
        this.aventurierRepository = aventurierRepository;
        this.aventurierMapper = aventurierMapper;
        this.queteRepository = queteRepository;
        this.participationEquipeRepository = participationEquipeRepository;
    }

    /**
     * Crée et enregistre un nouvel aventurier.
     *
     * @param dto les données de l'aventurier à créer
     * @return le DTO de l'aventurier créé
     */
    public AventurierDTO save(AventurierInputDTO dto) {
        Aventurier aventurier = aventurierRepository.save(aventurierMapper.toEntity(dto));
        return aventurierMapper.toDTO(aventurier);
    }

    /**
     * Récupère la liste de tous les aventuriers.
     *
     * @return la liste des DTOs de tous les aventuriers
     */
    public List<AventurierDTO> getAllAventurier() {
        return aventurierRepository.findAll()
                .stream()
                .map(aventurierMapper::toDTO)
                .toList();
    }

    /**
     * Récupère un aventurier par son id.
     *
     * @param id l'identifiant de l'aventurier
     * @return le DTO de l'aventurier trouvé
     * @throws NotFoundException si l'aventurier n'existe pas
     */
    public AventurierDTO getAventurierById(Long id) {
        return aventurierRepository.findById(id)
                .map(aventurierMapper::toDTO)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    /**
     * Met à jour un aventurier existant.
     *
     * @param dto les nouvelles données de l'aventurier
     * @return le DTO de l'aventurier mis à jour
     * @throws NotFoundException si l'aventurier n'existe pas
     */
    public AventurierDTO update(AventurierUpdateDTO dto) {
        Aventurier aventurier = aventurierRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        aventurierMapper.updateEntityFromDTO(dto, aventurier);
        Aventurier updated = aventurierRepository.save(aventurier);
        return aventurierMapper.toDTO(updated);
    }

    /**
     * Supprime un aventurier par son id.
     *
     * @param id l'identifiant de l'aventurier à supprimer
     * @throws NotFoundException si l'aventurier n'existe pas
     */
    public void delete(Long id) {
        if (!aventurierRepository.existsById(id)) {
            throw new NotFoundException(NOT_FOUND);
        }
        aventurierRepository.deleteById(id);
    }

    /**
     * Récupère les détails complets d'un aventurier incluant son historique de missions
     * et ses informations de repos.
     *
     * @param id l'identifiant de l'aventurier
     * @return le DTO détaillé de l'aventurier
     * @throws NotFoundException si l'aventurier n'existe pas
     */
    public AventurierDetailDTO getAventurierDetail(Long id) {
        Aventurier aventurier = aventurierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));

        AventurierDetailDTO detailDTO = new AventurierDetailDTO();

        // Informations de base
        detailDTO.setAventurier(aventurierMapper.toDTO(aventurier));

        // Récupère toutes les participations de l'aventurier
        List<ParticipationEquipe> participations = participationEquipeRepository.findByAventurierId(id);

        // Récupère toutes les quêtes de l'aventurier
        List<Quete> quetes = queteRepository.findQuetesByAventurierId(id);

        // Construction de l'historique des missions
        List<MissionHistoriqueDTO> missionsEnCours = new ArrayList<>();
        List<MissionHistoriqueDTO> missionsPassees = new ArrayList<>();
        int joursTotauxRepos = 0;

        for (ParticipationEquipe participation : participations) {
            // Trouve la quête correspondante
            Quete quete = quetes.stream()
                    .filter(q -> participation.getEquipe() != null &&
                                 q.getEquipeId() != null &&
                                 q.getEquipeId().equals(participation.getEquipe().getId()))
                    .findFirst()
                    .orElse(null);

            if (quete != null) {
                MissionHistoriqueDTO missionDTO = new MissionHistoriqueDTO();
                missionDTO.setId(quete.getId());
                missionDTO.setNomQuete(quete.getNom());
                missionDTO.setStatut(quete.getStatut().name());
                missionDTO.setDateDebut(participation.getDateAffectation());
                missionDTO.setDateRetour(participation.getDateRetour());
                missionDTO.setExperienceGagnee(participation.getGainExperience());
                missionDTO.setEtat(participation.getEtat());

                // Calcul de la durée réelle
                if (participation.getDateAffectation() != null && participation.getDateRetour() != null) {
                    long duree = ChronoUnit.DAYS.between(
                            participation.getDateAffectation(),
                            participation.getDateRetour());
                    missionDTO.setDureeReelle((int) duree);
                }

                // Classification mission en cours ou passée
                if ("EN_COURS".equals(quete.getStatut().name()) || "NOUVELLE".equals(quete.getStatut().name())) {
                    missionsEnCours.add(missionDTO);
                } else {
                    missionsPassees.add(missionDTO);

                    // Calcule le repos passé pour cette mission
                    if (participation.getDateRetour() != null && quete.getExperienceRecommandee() != null) {
                        // Estimation simple du repos passé
                        long dureeMission = participation.getDateAffectation() != null ?
                                ChronoUnit.DAYS.between(participation.getDateAffectation(), participation.getDateRetour()) : 0;
                        if (dureeMission > 0) {
                            int expRec = quete.getExperienceRecommandee();
                            int expR = aventurier.getNiveauExperience();
                            if (expR > 0) {
                                double repos = 0.5 * ((double) expRec / expR) * dureeMission;
                                joursTotauxRepos += (int) Math.ceil(repos);
                            }
                        }
                    }
                }
            }
        }

        detailDTO.setMissionsEnCours(missionsEnCours);
        detailDTO.setMissionsPassees(missionsPassees);
        detailDTO.setJoursTotauxRepos(joursTotauxRepos);

        // Calcule le repos actuel si l'aventurier est EN_REPOS
        if ("EN_REPOS".equals(aventurier.getDisponibilite()) && aventurier.getDateDisponibilite() != null) {
            ReposSamDTO reposDTO = new ReposSamDTO();

            // Trouve la dernière mission terminée
            ParticipationEquipe derniere = participations.stream()
                    .filter(p -> p.getDateRetour() != null)
                    .max((p1, p2) -> p1.getDateRetour().compareTo(p2.getDateRetour()))
                    .orElse(null);

            if (derniere != null) {
                LocalDate dateDebutRepos = derniere.getDateRetour();
                LocalDate dateFinRepos = aventurier.getDateDisponibilite();
                long dureeRepos = ChronoUnit.DAYS.between(dateDebutRepos, dateFinRepos);

                reposDTO.setDureeReposJours((int) dureeRepos);
                reposDTO.setDateDebutRepos(dateDebutRepos);
                reposDTO.setDateFinRepos(dateFinRepos);

                // Trouve la quête correspondante
                Quete derniereQuete = quetes.stream()
                        .filter(q -> derniere.getEquipe() != null &&
                                     q.getEquipeId() != null &&
                                     q.getEquipeId().equals(derniere.getEquipe().getId()))
                        .findFirst()
                        .orElse(null);

                if (derniereQuete != null) {
                    reposDTO.setDerniereMissionId(derniereQuete.getId());
                    reposDTO.setDerniereMissionNom(derniereQuete.getNom());
                }
            }

            detailDTO.setReposActuel(reposDTO);
        }

        return detailDTO;
    }

}
