package com.data.kanyh.mapper;


import com.data.kanyh.dto.QueteDTO;
import com.data.kanyh.dto.QueteInputDTO;
import com.data.kanyh.model.Quete;
import com.data.kanyh.model.Specialite;
import com.data.kanyh.repository.SpecialiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class QueteMapper {

    @Autowired
    private SpecialiteRepository specialiteRepository;

    /**
     * Convertit une entité {@link Quete} en {@link QueteDTO}.
     * @param quete l'entité {@link Quete} à convertir
     * @return le {@link QueteDTO} correspondant
     */
    public QueteDTO toDTO(Quete quete){
        QueteDTO dto = new QueteDTO();
        dto.setId(quete.getId());
        dto.setNom(quete.getNom());
        dto.setDescription(quete.getDescription());
        dto.setPrime(quete.getPrime());
        dto.setDureeEstimee(quete.getDureeEstimee());
        dto.setDatePeremption(quete.getDatePeremption());
        dto.setExperienceGagnee(quete.getExperienceGagnee());
        dto.setStatut(quete.getStatut().name());
        dto.setCommanditaireId(quete.getCommanditaireId());
        dto.setEquipeId(quete.getEquipeId());
        dto.setSpecialitesRequises(quete.getSpecialitesRequises());
        dto.setDateCreation(quete.getDateCreation());
        return dto;
    }

    /**
     * Convertit un {@link QueteInputDTO} en entité {@link Quete}.
     * Cette méthode est utilisée lors de la création d'une nouvelle quête.
     * Le statut est automatiquement défini à {@code NOUVELLE}, indépendamment
     * de la valeur fournie dans le DTO d'entrée.
     * @param input le {@link QueteInputDTO} contenant les données de la nouvelle quête
     * @return l'entité {@link Quete} correspondante avec le statut NOUVELLE
     */
    public Quete toEntity(QueteInputDTO input){
        Quete quete = new Quete();
        quete.setNom(input.getNom());
        quete.setDescription(input.getDescription());
        quete.setPrime(input.getPrime());
        quete.setDureeEstimee(input.getDureeEstimee());
        quete.setDatePeremption(input.getDatePeremption());
        quete.setStatut(com.data.kanyh.model.StatutQuete.NOUVELLE);
        quete.setDateCreation(LocalDate.now());

        // Convertir les IDs de spécialités en objets Specialite
        if (input.getSpecialitesRequisesIds() != null && !input.getSpecialitesRequisesIds().isEmpty()) {
            List<Specialite> specialites = input.getSpecialitesRequisesIds().stream()
                    .map(id -> specialiteRepository.findById(Integer.parseInt(id))
                            .orElseThrow(() -> new RuntimeException("Specialite non trouvée avec l'id: " + id)))
                    .collect(Collectors.toList());
            quete.setSpecialitesRequises(specialites);
        } else {
            quete.setSpecialitesRequises(new ArrayList<>());
        }

        return quete;
    }

    /**
     * Met à jour une entité {@link Quete} existante avec les valeurs d'un {@link QueteInputDTO}.
     * Cette méthode effectue une mise à jour partielle : seuls les champs non-null
     * du DTO d'entrée sont appliqués à l'entité. Les champs null sont ignorés,
     * permettant ainsi de ne mettre à jour que les champs souhaités sans affecter les autres.
     * @param dto le {@link QueteInputDTO} contenant les nouvelles valeurs
     * @param entity l'entité {@link Quete} à mettre à jour (modifiée en place)
     */
    public void updateEntityFromDTO(QueteInputDTO dto, Quete entity) {
        if (dto.getNom() != null) {
            entity.setNom(dto.getNom());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getPrime() != null) {
            entity.setPrime(dto.getPrime());
        }
        if (dto.getDureeEstimee() != null) {
            entity.setDureeEstimee(dto.getDureeEstimee());
        }
        if (dto.getDatePeremption() != null) {
            entity.setDatePeremption(dto.getDatePeremption());
        }

        // Mettre à jour les spécialités requises si fournies
        if (dto.getSpecialitesRequisesIds() != null) {
            List<Specialite> specialites = dto.getSpecialitesRequisesIds().stream()
                    .map(id -> specialiteRepository.findById(Integer.parseInt(id))
                            .orElseThrow(() -> new RuntimeException("Specialite non trouvée avec l'id: " + id)))
                    .collect(Collectors.toList());
            entity.setSpecialitesRequises(specialites);
        }
    }
}
